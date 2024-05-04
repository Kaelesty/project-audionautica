package com.kaelesty.audionautica.data.playlists

data class AddTrackToPlaylistRequest(
	val track_id: Int,
	val playlist_id: Int,
)
