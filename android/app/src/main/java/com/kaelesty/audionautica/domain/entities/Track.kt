package com.kaelesty.audionautica.domain.entities

import android.net.Uri

data class Track(
	val id: Int,
	val title: String,
	val artist: String,
	val duration: Long,
	val musicFile: Uri,
	val posterFile: Uri?
)