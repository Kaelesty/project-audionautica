package com.kaelesty.audionautica.domain.localplaylists


class LocalPlaylist(
	val id: Int,
	val title: String,
	val desc: String,
	val isPullable: Boolean,
	val remoteId: Int,
	val trackIds: List<Int>,
)