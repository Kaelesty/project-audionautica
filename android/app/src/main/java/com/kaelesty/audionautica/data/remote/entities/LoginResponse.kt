package com.kaelesty.audionautica.data.remote.entities

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@SerializedName("token") val jwt: String,
)