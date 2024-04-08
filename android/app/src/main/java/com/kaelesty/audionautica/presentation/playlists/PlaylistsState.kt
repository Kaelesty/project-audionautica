package com.kaelesty.audionautica.presentation.playlists

import com.kaelesty.audionautica.domain.playlists.UserPlaylists

sealed class PlaylistsState {

	object Loading: PlaylistsState()

	data class Result(
		val cards: List<PlaylistCardState>
	): PlaylistsState()

	object Empty: PlaylistsState()
}