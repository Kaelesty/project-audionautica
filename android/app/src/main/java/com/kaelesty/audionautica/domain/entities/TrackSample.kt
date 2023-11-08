package com.kaelesty.audionautica.domain.entities

import com.kaelesty.audionautica.R

data class TrackSample(
	val id: Int,
	val info: TrackInfo = TrackInfo()
)

data class TrackInfo(
	val name: String = "Akeboshi",
	val artist: String = "re:Tye",
	val poster: Int = R.drawable.example_track_poster_2
)