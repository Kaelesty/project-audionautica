package com.kaelesty.audionautica.data.playlists

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.mappers.PlaylistMapper
import com.kaelesty.audionautica.data.repos.AccessRepo
import com.kaelesty.audionautica.data.repos.tools.FileUploaderTool
import com.kaelesty.audionautica.data.repos.tools.JwtTool
import com.kaelesty.audionautica.data.repos.tools.UrlTool
import com.kaelesty.audionautica.domain.playlists.IPlaylistsRepo
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.domain.playlists.UserPlaylists
import java.io.IOException
import javax.inject.Inject

class PlaylistsRepo @Inject constructor(
	private val apiService: PlaylistsApiService,
	private val dao: PlaylistDao,
	private val jwtTool: JwtTool,
	private val urlTool: UrlTool,
	private val uploader: FileUploaderTool,
	private val playlistMapper: PlaylistMapper,
	private val accessRepo: AccessRepo,
): IPlaylistsRepo {

	val PLAYLIST_TRACKIDS_DELIMITER = "/"

	override suspend fun nukeTable() {
		dao.nukeTable()
	}

	override suspend fun updatePlaylistCollection() {
		try {
			val response = apiService.getPlaylists(
				token = jwtTool.getToken()
			)
			when (response.code()) {
				200 -> {
					response.body()?.let {
						(it.created + it.liked).forEach { id ->
							val playlist = loadEntirePlaylist(id)
							if (playlist is UserPlaylist.Entire) {
								dao.updatePlaylist(
									playlistMapper.domainToDbModel(playlist)
								)
							}
						}
					}
				}
				418 -> {
					accessRepo.logout()
				}
				else -> {

				}
			}
		} catch (e: Exception) {

		}
	}

	override fun getPlaylistCollection(): LiveData<List<UserPlaylist.Entire>> {
		return dao.getAll().map { list ->
			list.mapNotNull {
				playlistMapper.DbModelToDomain(it)
			}
		}
	}

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
				418 -> {
					accessRepo.logout()
					return UserPlaylists.Error
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
			if (response.code() == 418) {
				accessRepo.logout()
			}
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
				418 -> {
					accessRepo.logout()
					throw IOException()
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