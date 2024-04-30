package com.kaelesty.audionautica.data.local.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kaelesty.audionautica.data.local.typeconverters.ListIntConverter

@Entity(tableName = "login")
class LoginDbModel(
	@PrimaryKey val login: String,
	val password: String,
)