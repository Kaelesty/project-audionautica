package com.kaelesty.audionautica.data.playlists

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.net.toFile
import com.kaelesty.audionautica.data.mappers.PlaylistMapper
import com.kaelesty.audionautica.data.remote.api.MusicApiService
import com.kaelesty.audionautica.data.repos.tools.FileUploaderTool
import com.kaelesty.audionautica.data.repos.tools.JwtTool
import com.kaelesty.audionautica.data.repos.tools.UrlTool
import com.kaelesty.audionautica.domain.playlists.IPlaylistsRepo
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.domain.playlists.UserPlaylists
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
	private val urlTool: UrlTool,
	private val uploader: FileUploaderTool,
	private val playlistMapper: PlaylistMapper
): IPlaylistsRepo {

	val PLAYLIST_TRACKIDS_DELIMITER = "/"

	override suspend fun getPlaylists(): UserPlaylists {
		try {
			val response = apiService.getPlaylists(
				token = jwtTool.getToken()
			)
			when (response.code()) {
				200 -> {
					response.body()?.let {
						return UserPlaylists.Result(
							created = it.created,
							liked = it.created
						)
					}
				}
				else -> {
					return UserPlaylists.Error
				}
			}
		} catch (e: Exception) {
			return UserPlaylists.Error
		}
		return UserPlaylists.Error
	}

	override suspend fun uploadPlaylist(playlist: UserPlaylist.Entire, uri: Uri): Boolean {
		return try {
			val response = apiService.uploadPlaylist(
				title = playlist.title,
				desc = playlist.desc,
				token = jwtTool.getToken(),
				trackIds = playlist.trackIds.joinToString(PLAYLIST_TRACKIDS_DELIMITER),
				isPrivate = playlist.isPrivate,
				posterFile = uploader.getMultipartOfFile(uri, "Image"),
				ext = uploader.getFileExtension(uri) ?: throw Exception(),
			)
			response.code() == 200
		} catch (e: Exception) {
			false
		}
	}

	override suspend fun likePlaylist(playlist_id: Int): Boolean {
		return try {
			val response = apiService.likePlaylist(
				token = jwtTool.getToken(),
				body = PlaylistIdRequest(
					playlist_id = playlist_id.toString()
				)
			)
			return response.code() == 200
		} catch (e: Exception) {
			false
		}
	}

	override suspend fun loadEntirePlaylist(playlist_id: Int): UserPlaylist {
		return try {
			val response = apiService.playlistInfoId(
				token = jwtTool.getToken(),
				body = EntirePlaylistRequest(
					id = playlist_id
				)
			)
			when (response.code()) {
				200 -> {
					playlistMapper.ResponseToDomain(
						id = playlist_id,
						res = response.body() ?: throw IOException(),
						imageUrl = urlTool.getPlaylistPosterUrl(
							id = playlist_id,
							token = jwtTool.getToken()
						)
					)
				}
				else -> {
					throw IOException()
				}
			}
		}
		catch (e: Exception) {
			Log.d("PlaylistRepo.kt", e.message.toString())
			UserPlaylist.IdOnly(playlist_id)
		}
	}
}