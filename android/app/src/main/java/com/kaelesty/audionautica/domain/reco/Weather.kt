package com.kaelesty.audionautica.domain.reco

data class Weather(
	val temp: Float, // *C
	val windForce: Float, // m/s
	val windDirection: Int, // *
	val waves: Int, // sea rough
	val pressure: Int // millimeters of mercury
)
