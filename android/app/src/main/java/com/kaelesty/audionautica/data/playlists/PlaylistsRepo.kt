package com.kaelesty.audionautica.data.playlists

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.net.toFile
import com.kaelesty.audionautica.data.remote.api.MusicApiService
import com.kaelesty.audionautica.data.repos.tools.FileUploaderTool
import com.kaelesty.audionautica.data.repos.tools.JwtTool
import com.kaelesty.audionautica.domain.playlists.IPlaylistsRepo
import com.kaelesty.audionautica.domain.playlists.Playlist
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class PlaylistsRepo @Inject constructor(
	private val apiService: PlaylistsApiService,
	private val jwtTool: JwtTool,
	private val uploader: FileUploaderTool,
	private val contentResolver: ContentResolver,
	private val application: Application
): IPlaylistsRepo {

	val PLAYLIST_TRACKIDS_DELIMITER = "/"

	override suspend fun getPlaylists(): List<Playlist> {
		TODO("Not yet implemented")
	}

	override suspend fun uploadPlaylist(playlist: Playlist, uri: Uri): Boolean {
		return try {
			val response = apiService.uploadPlaylist(
				title = playlist.title,
				creatorId = playlist.creatorId,
				desc = playlist.desc,
				token = jwtTool.getToken(),
				trackIds = playlist.trackIds.joinToString(PLAYLIST_TRACKIDS_DELIMITER),
				isPrivate = playlist.isPrivate,
				posterFile = uploader.getMultipartOfFile(uri, "Image"),
				ext = uploader.getFileExtension(uri) ?: throw Exception(),
			)
			response.code() == 200
		} catch (e: Exception) {
			val d = e
			false
		}
	}
}