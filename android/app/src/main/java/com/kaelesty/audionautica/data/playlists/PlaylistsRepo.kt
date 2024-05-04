package com.kaelesty.audionautica.data.playlists

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.mappers.PlaylistMapper
import com.kaelesty.audionautica.data.remote.entities.SearchDto
import com.kaelesty.audionautica.data.repos.AccessRepo
import com.kaelesty.audionautica.data.repos.tools.CacheTool
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
	private val cacheTool: CacheTool,
): IPlaylistsRepo {

	private val PLAYLIST_TRACKIDS_DELIMITER = "/"

	override suspend fun nukeTable() {
		cacheTool.clearPlaylistsCache()
	}

	override suspend fun updatePlaylistTrackset(playlistId: Int, trackIds: List<Int>): Boolean {
		return try {
			val response = apiService.updatePlaylistTrackset(
				token = jwtTool.getToken(),
				body = UpdatePlaylistTracksetRequest(
					playlistId, trackIds
				)
			)
			when (response.code()) {
				200 -> {
					updatePlaylistById(playlistId)
					true
				}
				418 -> {
					accessRepo.logout()
					false
				}
				else -> {
					false
				}
			}
		} catch (e: Exception) {
			false
		}
	}

	override suspend fun searchPlaylists(query: String): List<UserPlaylist.Entire> {
		return try {
			val response = apiService.searchPlaylists(
				token = jwtTool.getToken(),
				body = SearchDto(
					query
				)
			)
			when (response.code()) {
				200 -> {
					response.body()?.playlists?.map { playlistMapper.IdResponseToDomain(
						res = it,
						imageUrl = urlTool.getPlaylistPosterUrl(
							id = it.id,
							token = jwtTool.getToken()
						)
					) } ?: listOf()
				}
				418 -> {
					accessRepo.logout()
					throw IOException()
				}
				else -> {
					throw IOException()
				}
			}
		} catch (e: Exception) {
			listOf()
		}
	}

	override suspend fun addTrackToPlaylist(trackId: Int, playlistId: Int): Boolean {
		return try {
			val response = apiService.addTrackToPlayList(
				token = jwtTool.getToken(),
				body = AddTrackToPlaylistRequest(
					track_id = trackId,
					playlist_id = playlistId,
				)
			)
			if (response.code() == 418) {
				accessRepo.logout()
			}
			if (response.code() == 200) {
				updatePlaylistById(playlistId)
				true
			}
			false
		} catch (e: Exception) {
			false
		}
	}

	private suspend fun updatePlaylistById(playlistId: Int) {
		val playlist = loadEntirePlaylist(playlistId)
		if (playlist is UserPlaylist.Entire) {
			dao.updatePlaylist(
				playlistMapper.domainToDbModel(playlist)
			)
		}
	}

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
							updatePlaylistById(id)
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

	override fun loadLiveEntirePlaylist(playlistId: Int): LiveData<UserPlaylist> {
		return dao.getLivePlaylist(playlistId).map { playlistMapper.DbModelToDomain(it) ?: UserPlaylist.IdOnly(playlistId) }
	}

	override suspend fun uploadPlaylist(playlist: UserPlaylist.Entire, uri: Uri): Boolean {
		return try {
			val file = uploader.getMultipartOfFile(uri, "Image")
			val response = apiService.uploadPlaylist(
				title = playlist.title,
				desc = playlist.desc,
				token = jwtTool.getToken(),
				trackIds = playlist.trackIds.joinToString(PLAYLIST_TRACKIDS_DELIMITER),
				isPrivate = playlist.isPrivate,
				posterFile = file,
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
			UserPlaylist.IdOnly(playlist_id)
		}
	}
}