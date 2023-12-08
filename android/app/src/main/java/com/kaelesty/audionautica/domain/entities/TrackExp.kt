package com.kaelesty.audionautica.domain.entities

import android.net.Uri

// Experimental class to check file-uploading abilities
// should be replaced by Track
data class TrackExp(
	val id: Int,
	val title: String,
	val artist: String,
	val duration: Long,
	val musicFile: Uri,
	val posterFile: Uri?
)