package com.kaelesty.audionautica.domain.playlists

import android.net.Uri
import androidx.lifecycle.LiveData

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

	suspend fun updatePlaylistCollection()

	fun getPlaylistCollection(): LiveData<List<UserPlaylist.Entire>>

	suspend fun nukeTable()
}