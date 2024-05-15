package com.kaelesty.audionautica.domain.playlists

import android.net.Uri
import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Track

interface IPlaylistsRepo {

	suspend fun addTrackToPlaylist(trackId: Int, playlistId: Int): Boolean

	suspend fun getPlaylists(): UserPlaylists

	suspend fun uploadPlaylist(
		playlist: UserPlaylist.Entire,
		uri: Uri
	): Boolean

	suspend fun likePlaylist(
		playlistId: Int
	): Boolean

	suspend fun dislikePlaylist(
		playlistId: Int
	): Boolean

	suspend fun deletePlaylist(
		playlistId: Int
	): Boolean

	suspend fun loadEntirePlaylist(
		playlist_id: Int
	): UserPlaylist

	fun loadLiveEntirePlaylist(
		playlist_id: Int
	): LiveData<UserPlaylist>

	suspend fun loadCachedPlaylist(
		playlist_id: Int,
		update: Boolean = false
	): UserPlaylist.Entire?

	suspend fun updatePlaylistCollection()

	fun getPlaylistCollection(): LiveData<List<UserPlaylist.Entire>>

	suspend fun nukeTable()

	suspend fun updatePlaylistTrackset(playlistId: Int, trackIds: List<Int>): Boolean

	suspend fun searchPlaylists(query: String): List<UserPlaylist.Entire>
}