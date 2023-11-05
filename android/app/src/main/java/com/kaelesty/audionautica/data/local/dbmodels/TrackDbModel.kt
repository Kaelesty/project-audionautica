package com.kaelesty.audionautica.data.local.dbmodels

import android.net.Uri
import androidx.room.Entity

@Entity(
	tableName = "tracks"
)
data class TrackDbModel(
	val id: Int,
	val title: String,
	val artist: String,
	val duration: Long,
	val musicFile: Uri,
	val posterFile: Uri
)