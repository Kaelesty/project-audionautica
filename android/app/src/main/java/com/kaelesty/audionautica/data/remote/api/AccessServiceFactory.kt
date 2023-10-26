package com.kaelesty.audionautica.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AccessServiceFactory {

	private const val URL = "https://f88d-188-170-84-169.ngrok-free.app" + "/Auth/"

	val apiService: AccessApiService = Retrofit.Builder()
		.baseUrl(URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(AccessApiService::class.java)
}