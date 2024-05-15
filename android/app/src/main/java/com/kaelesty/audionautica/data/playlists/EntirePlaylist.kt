package com.kaelesty.audionautica.data.playlists

import com.google.gson.annotations.SerializedName

data class EntirePlaylistResponse(
	@SerializedName("title") val title: String,
	@SerializedName("description") val description: String,
	@SerializedName("likes") val likes: Int,
	@SerializedName("isprivat") val isPrivate: Boolean,
	@SerializedName("editable") val isEditable: Boolean,
	@SerializedName("tracksid") val trackIds: String,
	@SerializedName("inCollection") val inCollection: Boolean,
)

data class EntirePlaylistRequest(
	@SerializedName("id") val id: Int
)

data class IdEntirePlaylistResponse(
	@SerializedName("id") val id: Int,
	@SerializedName("title") val title: String,
	@SerializedName("description") val description: String,
	@SerializedName("likes") val likes: Int,
	@SerializedName("isprivat") val isPrivate: Boolean,
	@SerializedName("editable") val isEditable: Boolean,
	@SerializedName("tracksid") val trackIds: String,
	@SerializedName("inCollection") val inCollection: Boolean = false
)
