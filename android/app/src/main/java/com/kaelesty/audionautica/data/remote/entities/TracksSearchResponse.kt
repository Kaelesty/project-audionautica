package com.kaelesty.audionautica.data.remote.entities

import com.google.gson.annotations.SerializedName

data class TracksSearchResponse(
	@SerializedName("tracks") val tracks: List<TrackDto>,
)