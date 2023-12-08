package com.kaelesty.audionautica.data.remote.entities

import com.google.gson.annotations.SerializedName

data class JwtDto(
	@SerializedName("token") val token: String
)
