package com.kaelesty.audionautica.presentation.playlists

import com.kaelesty.audionautica.domain.playlists.Playlist

sealed class PlaylistCardState {

	data class IdOnly(val id: Int): PlaylistCardState()

	data class Full(val playlist: Playlist): PlaylistCardState()
}