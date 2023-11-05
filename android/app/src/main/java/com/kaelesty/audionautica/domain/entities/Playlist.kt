package com.kaelesty.audionautica.domain.entities

import android.net.Uri

data class Playlist(
	val id: Int,
	val posterFile: Uri,
	val title: Uri,
	val trackIds: List<Int>
)