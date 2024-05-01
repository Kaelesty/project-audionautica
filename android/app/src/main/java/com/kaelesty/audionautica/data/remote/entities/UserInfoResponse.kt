package com.kaelesty.audionautica.data.remote.entities

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
	@SerializedName("username") val username: String,
	@SerializedName("email") val email: String,
)