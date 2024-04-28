package com.kaelesty.audionautica.data.reco

import com.kaelesty.audionautica.data.remote.entities.CheckAuthResponse
import com.kaelesty.audionautica.data.remote.entities.JwtDto
import com.kaelesty.audionautica.data.remote.entities.LoginDto
import com.kaelesty.audionautica.data.remote.entities.LoginResponse
import com.kaelesty.audionautica.data.remote.entities.RegisterDto
import com.kaelesty.audionautica.data.remote.entities.RegisterResponse
import com.kaelesty.audionautica.di.ApplicationScope
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

@ApplicationScope
interface RecoApiService {
	@POST("WeatherReco/")
	suspend fun getWeatherReco(
		@Header("token") token: String,
		@Body body: WeatherRecoRequest
	): Response<WeatherResponse>
}