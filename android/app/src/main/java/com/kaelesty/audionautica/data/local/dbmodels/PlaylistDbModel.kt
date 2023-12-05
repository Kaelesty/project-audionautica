package com.kaelesty.audionautica.data.local.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kaelesty.audionautica.data.local.typeconverters.ListIntConverter

@Entity(
	tableName = "playlists-"
)
class PlaylistDbModel(
	@PrimaryKey val id: Int,
	val title: String,
	@field:TypeConverters(ListIntConverter::class) val trackIds: List<Int>
)