package com.kaelesty.audionautica.domain.entities

data class Track(
	val id: Int,
	val title: String,
	val artist: String,
	val tags: List<String>
)

