package com.kaelesty.audionautica.data.remote.entities

data class TrackInfoIdRequest(
	val id: Int
)

data class TrackInfoIdResponse(
	val title: String,
	val artist: String,
	val tags: String,
)