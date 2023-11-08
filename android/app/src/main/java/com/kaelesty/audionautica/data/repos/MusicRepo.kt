package com.kaelesty.audionautica.data.repos

import android.content.ContentResolver
import android.net.Uri
import android.os.FileUtils
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.mappers.TrackMapper
import com.kaelesty.audionautica.data.remote.api.MusicApiService
import com.kaelesty.audionautica.di.ApplicationScope
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URI
import javax.inject.Inject

@ApplicationScope
class MusicRepo @Inject constructor(
	private val trackDao: TrackDao,
	private val playlistDao: PlaylistDao,
	private val trackMapper: TrackMapper,
	private val musicApiService: MusicApiService,
	private val contentResolver: ContentResolver
): IMusicRepo {


	override suspend fun logout() {
		// TODO delete JWT
	}

	override fun getTracks(): LiveData<List<Track>> {
		return trackDao.getAll().map { it.map { dbModel -> trackMapper.dbModelToDomain(dbModel) } }
	}

	override suspend fun addTrack(track: Track) {
		// TODO upload to Server Database
		trackDao.createTrack(trackMapper.domainToDbModel(track))
	}

	override suspend fun uploadTrack(track: Track) {
		val file = File(track.musicFile.path)
		val requestFile = RequestBody.create(
			MediaType.parse(contentResolver.getType(track.musicFile)?: return),
			file
		)
		val body = MultipartBody.Part.createFormData(
			"track", file.name, requestFile
		)
		val description = RequestBody.create(
			MultipartBody.FORM, "description"
		)
		val request = musicApiService.uploadTrack(
			description, body
		)
		Log.d("MUSIC_REPO", request.code().toString())
	}
}