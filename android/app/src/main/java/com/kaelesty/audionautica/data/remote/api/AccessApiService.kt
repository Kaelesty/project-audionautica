package com.kaelesty.audionautica.data.remote.api

import com.kaelesty.audionautica.data.remote.entities.LoginDto
import com.kaelesty.audionautica.data.remote.entities.LoginResponse
import com.kaelesty.audionautica.data.remote.entities.RegisterDto
import com.kaelesty.audionautica.data.remote.entities.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AccessApiService {

	@POST("Login")
	suspend fun login(@Body loginDto: LoginDto): Response<LoginResponse>

	@POST("Register")
	suspend fun register(@Body registerDto: RegisterDto): Response<RegisterResponse>
}