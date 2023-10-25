package com.kaelesty.audionautica.data.remote.entities

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@SerializedName("jwt") val jwt: String,
)