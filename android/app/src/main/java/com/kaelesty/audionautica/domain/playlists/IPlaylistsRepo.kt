package com.kaelesty.audionautica.domain.playlists

import android.net.Uri

interface IPlaylistsRepo {

	suspend fun getPlaylists(): UserPlaylists

	suspend fun uploadPlaylist(
		playlist: UserPlaylist.Entire,
		uri: Uri
	): Boolean

	suspend fun likePlaylist(
		playlist_id: Int
	): Boolean

	suspend fun loadEntirePlaylist(
		playlist_id: Int
	): UserPlaylist
}