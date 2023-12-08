package com.kaelesty.audionautica.data.local.typeconverters

import androidx.room.TypeConverter

class ListIntConverter {

	@TypeConverter
	fun fromIntList(list: List<Int>): String = list.joinToString("%")

	@TypeConverter
	fun toIntList(string: String): List<Int> {
		return try {
			string.split("%").map { it.toInt() }
		}
		catch (e: Exception) {
			listOf()
		}
	}
}