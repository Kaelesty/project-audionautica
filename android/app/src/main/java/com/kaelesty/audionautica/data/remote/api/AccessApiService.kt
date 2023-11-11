package com.kaelesty.audionautica.data.remote.api

import com.kaelesty.audionautica.data.remote.entities.CheckAuthResponse
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
interface AccessApiService {
	@POST("Login/")
	suspend fun login(@Body loginDto: LoginDto): Response<String>

	@POST("Register/")
	suspend fun register(@Body registerDto: RegisterDto): Response<RegisterResponse>

	@GET("CheckConnection/")
	suspend fun checkConnection(): Response<Unit>

	@GET("CheckAuth/")
	suspend fun checkAuth(@Header("jwt") jwt: String): Response<CheckAuthResponse>
}