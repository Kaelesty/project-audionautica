package com.kaelesty.audionautica.data.local.dbmodels

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "playlists"
)
class PlaylistDbModel(
	@PrimaryKey val id: Int,
	val posterFile: String?,
	val title: String,
	//val trackIds: List<Int>
)