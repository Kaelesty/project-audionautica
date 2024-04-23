package com.kaelesty.audionautica.domain.playlists

import android.net.Uri

data class Playlist(
	val id: Int,
	val title: String,
	val creatorId: Int,
	val trackIds: List<Int>,
	val imageUrl: String,
	val desc: String,
	val isPrivate: Boolean
)