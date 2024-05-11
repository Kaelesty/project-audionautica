package com.kaelesty.audionautica.data.localplaylists

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kaelesty.audionautica.data.local.typeconverters.ListIntConverter

@Entity(tableName = "local-playlists")
class LocalPlaylistDbModel(
	@PrimaryKey val id: Int,
	val title: String,
	val desc: String,
	val isPullable: Boolean,
	val remoteId: Int,
	@field:TypeConverters(ListIntConverter::class) val trackIds: List<Int>)