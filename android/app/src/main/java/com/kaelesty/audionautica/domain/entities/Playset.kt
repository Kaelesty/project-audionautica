package com.kaelesty.audionautica.domain.entities

data class Playset(
	val tracks: List<Track>,
	var cursor: Int
)