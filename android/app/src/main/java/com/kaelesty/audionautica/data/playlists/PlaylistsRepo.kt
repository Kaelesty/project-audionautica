package com.kaelesty.audionautica.data.playlists

import android.net.Uri
import com.kaelesty.audionautica.data.remote.api.MusicApiService
import com.kaelesty.audionautica.data.repos.tools.FileUploaderTool
import com.kaelesty.audionautica.data.repos.tools.JwtTool
import com.kaelesty.audionautica.domain.playlists.IPlaylistsRepo
import com.kaelesty.audionautica.domain.playlists.Playlist
import javax.inject.Inject

class PlaylistsRepo @Inject constructor(
	private val apiService: PlaylistsApiService,
	private val jwtTool: JwtTool,
	private val uploader: FileUploaderTool,
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
				ext = uri.path.toString().substring(uri.path.toString().lastIndexOf(".") + 1)
			)
			response.code() == 200
		} catch (e: Exception) {
			false
		}
	}
}