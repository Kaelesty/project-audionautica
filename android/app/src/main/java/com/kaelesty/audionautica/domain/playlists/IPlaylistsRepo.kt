package com.kaelesty.audionautica.domain.playlists

import android.net.Uri

interface IPlaylistsRepo {

	suspend fun getPlaylists(): List<Playlist>

	suspend fun uploadPlaylist(
		playlist: Playlist,
		uri: Uri
	): Boolean
}