package com.kaelesty.audionautica.data.remote.entities

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@SerializedName("email") val email: String,
	@SerializedName("username") val name: String,
	@SerializedName("id") val id: Int
)