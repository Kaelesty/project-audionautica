package com.kaelesty.audionautica.data.reco

import com.google.gson.annotations.SerializedName
import com.kaelesty.audionautica.data.remote.entities.TrackDto

data class WeatherRecoRequest(
	@SerializedName("temp") val temp: Float, // *C
	@SerializedName("windForce") val windForce: Float, // m/s
	@SerializedName("windDirection") val windDirection: Int, // *
	@SerializedName("waves") val waves: Int, // sea rough
 	@SerializedName("pressure") val pressure: Int, // millimeters of mercury
	@SerializedName("num") val num: Int
)

data class WeatherResponse(
	@SerializedName("tracks") val tracks: List<TrackDto>
)