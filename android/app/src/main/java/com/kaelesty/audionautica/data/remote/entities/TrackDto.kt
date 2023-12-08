package com.kaelesty.audionautica.data.remote.entities

import com.google.gson.annotations.SerializedName

data class TrackDto(
	@SerializedName("artist") val artist: String,
	@SerializedName("title") val title: String,
	@SerializedName("tags") val tags: String,
	@SerializedName("id") val id: Int,
)