package com.kaelesty.audionautica.domain.entities

data class TracksToPlay(
	val tracks: List<Track>,
	val dropQueue: Boolean
)