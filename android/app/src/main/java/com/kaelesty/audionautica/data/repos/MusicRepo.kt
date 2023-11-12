package com.kaelesty.audionautica.data.repos

import android.app.Application
import android.content.ContentResolver
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.mappers.TrackMapper
import com.kaelesty.audionautica.data.remote.api.MusicApiService
import com.kaelesty.audionautica.di.ApplicationScope
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackExp
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import com.kaelesty.audionautica.domain.returncodes.UploadTrackRC
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@ApplicationScope
class MusicRepo @Inject constructor(
	private val trackDao: TrackDao,
	private val trackMapper: TrackMapper,
	private val musicApiService: MusicApiService,
	private val contentResolver: ContentResolver,
	private val application: Application,
) : IMusicRepo {

	override fun getTracks(): LiveData<List<Track>> {
		return trackDao.getAll().map { it.map { dbModel -> trackMapper.dbModelToDomain(dbModel) } }
	}

	override suspend fun addTrack(track: TrackExp) {
		// TODO upload to Server Database
		//trackDao.createTrack(trackMapper.domainToDbModel(track))
	}

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override suspend fun uploadTrack(track: TrackExp): UploadTrackRC {
		try {
			track.musicFile.path?.let { Log.e("MYTAG", it) }
			val uri = track.musicFile

			val tempFile = File(application.cacheDir, "tempfile")
			try {
				val inputstream: InputStream = contentResolver.openInputStream(uri)!!
				val output = FileOutputStream(tempFile)
				val buffer = ByteArray(1024)
				var size: Int
				while (inputstream.read(buffer).also { size = it } != -1) {
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
			val request = musicApiService.uploadTrack(
				description, body
			)
			// TODO check offline mode
			return when(request.code()) {
				200 -> UploadTrackRC.OK
				else -> UploadTrackRC.SERVER_ERROR
			}
		}
		catch (e: IOException) {
			Log.e("MYTAG", e.message.toString())
			return UploadTrackRC.SERVER_ERROR
		}
	}

	override suspend fun search(query: String): List<Track> {
		try {
			val response = musicApiService.searchTracks()
			response.body()?.let { body ->
				if (response.code() == 200) {
					return body.tracks.map { trackMapper.dtoToDomain(it) }
				}
			}
			throw IllegalStateException("Tracks search failed.")
		} catch (e: Exception) {
			Log.e("AudionauticaLog", e.message.toString())
			return listOf()
		}
	}
}