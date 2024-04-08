package com.kaelesty.audionautica.domain.playlists

import android.net.Uri

interface IPlaylistsRepo {

	suspend fun getPlaylists(): UserPlaylists

	suspend fun uploadPlaylist(
		playlist: Playlist,
		uri: Uri
	): Boolean

	suspend fun likePlaylist(
		playlist_id: Int
	): Boolean
}