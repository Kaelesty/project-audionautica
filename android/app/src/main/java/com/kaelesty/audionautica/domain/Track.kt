package com.kaelesty.audionautica.domain

import com.kaelesty.audionautica.R

data class Track(
	val id: Int,
	val info: TrackInfo = TrackInfo()
)

data class TrackInfo(
	val name: String = "Idol",
	val artist: String = "YOASOBI",
	val poster: Int = R.drawable.example_track_poster
)