package com.kaelesty.audionautica.data.playlists

data class UserPlaylistResponse(
	val liked: List<Int>,
	val created: List<Int>
)