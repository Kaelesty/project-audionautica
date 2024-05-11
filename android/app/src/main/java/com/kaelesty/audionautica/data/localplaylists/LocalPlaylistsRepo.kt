package com.kaelesty.audionautica.data.localplaylists

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kaelesty.audionautica.data.playlists.EntirePlaylistRequest
import com.kaelesty.audionautica.data.playlists.PlaylistsApiService
import com.kaelesty.audionautica.data.playlists.UpdatePlaylistTracksetRequest
import com.kaelesty.audionautica.data.repos.AccessRepo
import com.kaelesty.audionautica.data.repos.tools.JwtTool
import com.kaelesty.audionautica.domain.localplaylists.ILocalPlaylistsRepo
import com.kaelesty.audionautica.domain.localplaylists.LocalPlaylist
import java.io.IOException
import javax.inject.Inject

class LocalPlaylistsRepo @Inject constructor(
	private val dao: LocalPlaylistDao,
	private val apiService: PlaylistsApiService,
	private val mapper: LocalPlaylistsMapper,
	private val jwtTool: JwtTool,
	private val accessRepo: AccessRepo,
) : ILocalPlaylistsRepo {

	override fun getLocalPlaylists(): LiveData<List<LocalPlaylist>> {
		return dao.getAll().map { playlist ->
			playlist.map {
				mapper.mapLocalPlaylistDbModelToDomain(it)
			}
		}
	}

	override suspend fun saveLocalPlaylist(playlist: LocalPlaylist) {
		dao.updatePlaylist(
			mapper.mapLocalPlaylistDomainToDbModel(playlist)
		)
	}

	override suspend fun deleteLocalPlaylist(playlistId: Int) {
		dao.deletePlaylist(playlistId)
	}

	override suspend fun pullLocalPlaylist(playlist: LocalPlaylist) {
		try {
			val response = apiService.playlistInfoId(
				token = jwtTool.getToken(),
				body = EntirePlaylistRequest(playlist.remoteId)
			)
			when (response.code()) {
				200 -> {
					dao.updatePlaylist(
						mapper.mapEntirePlaylistResponseToLocalPlaylistDbModel(
							playlist.remoteId,
							response.body() ?: throw IOException()
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
			Log.e("LocalUpdateError", "Error when pulling playlist")
		}
	}

	override suspend fun pushLocalPlaylist(playlist: LocalPlaylist) {
		try {
			apiService.updatePlaylistTrackset(
				token = jwtTool.getToken(),
				body = UpdatePlaylistTracksetRequest(
					playlistId = playlist.id,
					trackIds = playlist.trackIds
				)
			)
		}
		catch (e: Exception) {
			Log.e("LocalUpdateError", "Error when pushing playlist")
		}
	}
}