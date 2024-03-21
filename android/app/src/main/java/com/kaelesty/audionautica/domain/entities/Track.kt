package com.kaelesty.audionautica.domain.entities

import android.net.Uri

data class Track(
	val id: Int,
	val title: String,
	val artist: String,
	val tags: List<String>
)

data class PlayReadyTrack(
	val metadata: Track,
	val uri: Uri
)

