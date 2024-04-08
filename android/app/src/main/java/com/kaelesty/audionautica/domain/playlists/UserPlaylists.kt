package com.kaelesty.audionautica.domain.playlists

sealed class UserPlaylists {

	object Loading: UserPlaylists()

	data class Result(
		val playlists_ids: List<Int>
	): UserPlaylists()

	object Empty: UserPlaylists()
}