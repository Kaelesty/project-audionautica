package com.kaelesty.audionautica.data.remote.entities

data class TrackDto(
	val id: Int,
	val artist: String,
	val title: String,
	val poster: String // url
)