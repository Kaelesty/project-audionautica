package com.kaelesty.audionautica.data.local.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kaelesty.audionautica.data.local.typeconverters.ListIntConverter

@Entity(tableName = "token")
class TokenDbModel(
	@PrimaryKey val id: Int,
	val jwt: String
)