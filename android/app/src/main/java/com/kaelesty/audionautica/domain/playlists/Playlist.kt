package com.kaelesty.audionautica.domain.playlists


sealed class UserPlaylist(
	val id: Int
) {

	class IdOnly(
		id: Int,
	): UserPlaylist(id)

	class Entire(
		id: Int,
		val title: String,
		val isEditable: Boolean,
		val trackIds: List<Int>,
		val imageUrl: String,
		val desc: String,
		val isPrivate: Boolean,
		val likes: Int,
		val inCollection: Boolean,
	): UserPlaylist(id)
}
