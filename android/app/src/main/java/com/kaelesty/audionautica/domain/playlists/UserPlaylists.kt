package com.kaelesty.audionautica.domain.playlists



sealed class UserPlaylists {

	object Error: UserPlaylists()

	data class Result(
		val liked: List<Int>,
		val created: List<Int>,
	): UserPlaylists()
}