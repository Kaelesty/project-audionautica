package com.kaelesty.audionautica.data.repos

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.mappers.PlaylistMapper
import com.kaelesty.audionautica.data.mappers.TrackMapper
import com.kaelesty.audionautica.data.remote.api.MusicApiService
import com.kaelesty.audionautica.data.remote.entities.DownloadTrackDto
import com.kaelesty.audionautica.data.remote.entities.SearchDto
import com.kaelesty.audionautica.data.repos.tools.JwtTool
import com.kaelesty.audionautica.di.ApplicationScope
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackExp
import com.kaelesty.audionautica.domain.entities.TracksToPlay
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import com.kaelesty.audionautica.domain.returncodes.UploadTrackRC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@ApplicationScope
class MusicRepo @Inject constructor(
	private val trackDao: TrackDao,
	private val playlistDao: PlaylistDao,
	private val trackMapper: TrackMapper,
	private val playlistMapper: PlaylistMapper,
	private val musicApiService: MusicApiService,
	private val contentResolver: ContentResolver,
	private val application: Application,
	private val jwtTool: JwtTool
) : IMusicRepo {

	private val _tracksQueue = MutableSharedFlow<TracksToPlay>()
	val tracksQueue: SharedFlow<TracksToPlay> get() = _tracksQueue

	private val playingTrackFlow = MutableSharedFlow<Track>()

	private val playingEndedFlow = MutableSharedFlow<Unit>()

	private var trackQueue = listOf<Track>()
	private var queueCursor = 0

	private val scope = CoroutineScope(Dispatchers.IO)

	private var skipPlayingEnded = false

	init {
		scope.launch {
			playingEndedFlow.collect {
				if (skipPlayingEnded) {
					skipPlayingEnded = false
					return@collect
				}
				Log.d("AudionauticaTag1", "PlayingEnded!")
				queueCursor += 1
				if (queueCursor < trackQueue.size) {
					playingTrackFlow.emit(
						trackQueue[queueCursor]
					)
				}
			}
		}
	}

	override suspend fun playNext() {
		queueCursor += 1
		Log.d("AudionauticaTag", "Now cursor is $queueCursor")
		if (queueCursor >= 0 && queueCursor < trackQueue.size) {
			playingTrackFlow.emit(trackQueue[queueCursor])
			skipPlayingEnded = true
		}
	}

	override suspend fun playPrev() {
		queueCursor -= 1
		if (queueCursor >= 0 && queueCursor < trackQueue.size) {
			playingTrackFlow.emit(trackQueue[queueCursor])
			skipPlayingEnded = true
		}
	}

	override fun getPlayingTrackFlow(): SharedFlow<Track> {
		return playingTrackFlow.asSharedFlow()
	}

	override fun getPlayingEndedFlow(): MutableSharedFlow<Unit> {
		return playingEndedFlow
	}

	override fun getTracks(): LiveData<List<Track>> {
		return trackDao.getAll().map { it.map { dbModel -> trackMapper.dbModelToDomain(dbModel) } }
	}


	suspend fun downloadTrack(id: Int) {
		Log.d("AudionauticaTag", "Downloading $id")
		try {
			val response = musicApiService.downloadTrackSample(
				token = jwtTool.getToken(),
				DownloadTrackDto(id)
			)
			val body = response.body() ?: throw IOException("Empty body!")
			saveFile(body, id)

		} catch (e: Exception) {
			Log.d("AudionauticaTag", e.message.toString())
		}
	}

	private fun saveFile(body: ResponseBody, id: Int) {
		var input: InputStream? = null
		try {
			input = body.byteStream()
			val fileName = "/$id.mp3"
			val pathWhereYouWantToSaveFile = application.filesDir.absolutePath + fileName
			val fos = FileOutputStream(pathWhereYouWantToSaveFile)
			fos.use { output ->
				val buffer = ByteArray(4 * 1024)
				var read: Int
				while (input.read(buffer).also { read = it } != - 1) {
					output.write(buffer, 0, read)
				}

				output.flush()

			}
		} catch (exception: Exception) {
			Log.e("MusicViewModel", exception.toString())
		} finally {
			input?.close()
		}
	}

	fun deleteTrackFile(trackId: Int) {
		val fileName = "/$trackId.mp3"
		val filepath = application.filesDir.absolutePath + fileName
		File(filepath).delete()
	}

	override suspend fun addTrack(track: TrackExp) {
		// TODO upload to Server Database
		//trackDao.createTrack(trackMapper.domainToDbModel(track))
	}

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override suspend fun uploadTrack(track: Track, uri: Uri): UploadTrackRC {
		try {

			val tempFile = File(application.cacheDir, "tempfile")
			try {
				val inputstream: InputStream = contentResolver.openInputStream(uri) !!
				val output = FileOutputStream(tempFile)
				val buffer = ByteArray(1024)
				var size: Int
				while (inputstream.read(buffer).also { size = it } != - 1) {
					output.write(buffer, 0, size)
				}
				inputstream.close()
				output.close()
			} catch (e: IOException) {
				Log.e("MYTAG", "COPYCODE ERROR")
			}
			val file = tempFile
			val requestBody = RequestBody.create(
				"Audio".toMediaTypeOrNull(),
				file
			)
			val body = MultipartBody.Part.create(requestBody)

			val description = RequestBody.create(
				MultipartBody.FORM, "description"
			)

			// TODO check possibility to send more than one file via multipart body
			// TODO there i send music file twice, it should be replaced by poster file
			val request = musicApiService.uploadTrack(
				track.title,
				track.artist,
				track.tags.joinToString(trackMapper.TRACK_TAGS_STRINGIFICATION_DELIMITER),
				token = jwtTool.getToken(),
				description,
				body,
			)
			return when (request.code()) {
				200 -> UploadTrackRC.OK
				else -> UploadTrackRC.SERVER_ERROR
			}
		} catch (e: IOException) {
			Log.e("MYTAG", e.message.toString())
			return UploadTrackRC.SERVER_ERROR
		}
	}

	override suspend fun search(query: String): List<Track> {
		try {
			val response = musicApiService.searchTracks(
				token = jwtTool.getToken(),
				SearchDto(query)
			)
			response.body()?.let { body ->
				if (response.code() == 200) {
					return body.tracks.map { trackMapper.dtoToDomain(it) }
				}
			}
			throw IllegalStateException("Tracks search failed.")
		} catch (e: Exception) {
			Log.e("AudionauticaTag", e.message.toString())
			return listOf()
		}
	}

	override suspend fun addToTracksQueue(track: List<Track>) {
		trackQueue = track
		queueCursor = 0
		Log.d("AudionauticaTag", "Cursor dropped!")
		playingTrackFlow.emit(
			trackQueue[queueCursor]
		)
	}


	override suspend fun getTrackUri(id: Int): Uri {

		if (! checkFileDownloaded(id)) {
			Log.d("AudionauticaTag", "Not downloaded $id")
			downloadTrack(id)
		}
		Log.d("AudionauticaTag", "Ready $id")
		return getFileUri(id)
	}

	private fun checkFileDownloaded(id: Int): Boolean {
		val fileName = "/$id.mp3"
		val path = application.filesDir.absolutePath + fileName
		val file = File(path)
		return file.exists()
	}

	private fun getFileUri(id: Int): Uri {
		val fileName = "/$id.mp3"
		val path = application.filesDir.absolutePath + fileName
		return File(path).toUri()
	}

	override fun getTracksQueueFlow(): SharedFlow<TracksToPlay> {
		return tracksQueue
	}

	override suspend fun addTrackToPlaylist(track: Track, playlistId: Int) {
		trackDao.createTrack(trackMapper.domainToDbModel(track))

		val playlist = playlistMapper.DbModelToDomain(
			playlistDao.getPlaylist(playlistId)
		)

		val newTrackIds = playlist.trackIds.toMutableList()
		newTrackIds.add(track.id)

		val newPlaylist = Playlist(
			id = playlist.id,
			title = playlist.title,
			trackIds = newTrackIds
		)
		playlistDao.createPlaylist(
			playlistMapper.DomainToDbModel(newPlaylist) // Will be replaced by OnConflictStrategy.REPLACE
		)
	}

	override fun getPlaylists(): LiveData<List<Playlist>> {
		return playlistDao.getAll().map { list ->
			list.map { dbModel ->
				playlistMapper.DbModelToDomain(dbModel)
			}
		}
	}

	override suspend fun removeTrackFromPlaylist(track: Track, playlistId: Int) {
		val playlist = playlistMapper.DbModelToDomain(playlistDao.getPlaylist(playlistId))
		val newPlaylist = Playlist(
			id = playlist.id,
			title = playlist.title,
			trackIds = playlist.trackIds.toMutableList().apply {
				remove(track.id)
			}
		)
		playlistDao.createPlaylist(
			playlistMapper.DomainToDbModel(newPlaylist)
		)
		Log.d("AudionauticaTag", "Removing ${track.title} from ${playlist.title}")
	}

	override fun getPlaylistTracks(id: Int): List<Track> {
		val playlist = playlistMapper.DbModelToDomain(playlistDao.getPlaylist(id))
		val result = trackDao.getById(playlist.trackIds)
		return result.map { trackMapper.dbModelToDomain(it) }
	}

	override suspend fun createPlaylist(playlist: Playlist) {
		playlistDao.createPlaylist(
			playlistMapper.DomainToDbModel(playlist)
		)
	}

	override suspend fun deletePlaylist(playlistId: Int) {
		playlistDao.deletePlaylist(playlistId)
		Log.d("AudionauticaTag", "deleting playlist $playlistId")
	}

	override suspend fun saveTrack(track: Track) {
		trackDao.createTrack(
			trackMapper.domainToDbModel(track)
		)
		downloadTrack(track.id)
	}

	override suspend fun deleteTrack(track: Track) {
		trackDao.deleteTrack(track.id)
		deleteTrackFile(trackId = track.id)
		val playlists = playlistDao.getAll().value?: return

		playlists.forEach { playlist ->
			if (track.id in playlist.trackIds) {
				val newPlaylist = Playlist(
					id = playlist.id!!, // playlist is taken from db so its id definitely cant be null
					title = playlist.title,
					trackIds = playlist.trackIds.toMutableList().apply {
						remove(track.id)
					}
				)
				playlistDao.createPlaylist(playlist)
			}
		}
	}
}