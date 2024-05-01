package com.kaelesty.audionautica.presentation.playlists.list

import com.kaelesty.audionautica.domain.playlists.UserPlaylist

sealed class PlaylistsState {

	object Loading: PlaylistsState()

	data class Result(
		val playlists: List<UserPlaylist>
	): PlaylistsState()

	object Empty: PlaylistsState()

	object Initial: PlaylistsState()
}