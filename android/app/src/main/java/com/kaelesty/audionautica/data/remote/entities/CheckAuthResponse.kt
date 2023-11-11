package com.kaelesty.audionautica.data.remote.entities

import com.google.gson.annotations.SerializedName

data class CheckAuthResponse(
	@SerializedName("auth") val auth: Boolean,
)