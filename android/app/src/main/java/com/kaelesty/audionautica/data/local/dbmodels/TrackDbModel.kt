package com.kaelesty.audionautica.data.local.dbmodels

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "tracks-"
)
data class TrackDbModel(
	@PrimaryKey val id: Int,
	val title: String,
	val artist: String,
	val poster: String
)