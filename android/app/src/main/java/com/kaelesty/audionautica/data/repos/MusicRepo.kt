package com.kaelesty.audionautica.data.repos

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.mappers.TrackMapper
import com.kaelesty.audionautica.data.remote.api.MusicApiService
import com.kaelesty.audionautica.di.ApplicationScope
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import com.kaelesty.audionautica.domain.returncodes.UploadTrackRC
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URI
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

	override suspend fun addTrack(track: Track) {
		// TODO upload to Server Database
		trackDao.createTrack(trackMapper.domainToDbModel(track))
	}
	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override suspend fun uploadTrack(track: Track): UploadTrackRC {
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
				MediaType.parse("Audio"),
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
}