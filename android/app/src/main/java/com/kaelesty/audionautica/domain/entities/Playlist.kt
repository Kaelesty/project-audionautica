package com.kaelesty.audionautica.domain.entities

import android.net.Uri

data class Playlist(
	val id: Int,
	val title: String,
	val trackIds: List<Int>
)