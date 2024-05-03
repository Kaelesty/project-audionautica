package com.kaelesty.audionautica.data.remote.entities

import com.google.gson.annotations.SerializedName

data class CheckAuthResponse(
	@SerializedName("name") val name: String,
	@SerializedName("login") val email: String,
)