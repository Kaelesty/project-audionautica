package com.kaelesty.audionautica.domain.entities

data class Track(
	val id: Int, // id from backend DB

	// string-contents
	val title: String,
	val artist: String,

	val poster: String // url-string for use with AsyncImage

	// This class does not contain any music bytes or a file link.
	// To get the Uri for playback in ExoPlayer, use GetTrackUriUseCase
)