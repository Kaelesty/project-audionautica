package com.kaelesty.audionautica.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AccessServiceFactory {

	private const val URL = "URL"

	val apiService: AccessApiService = Retrofit.Builder()
		.baseUrl(URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(AccessApiService::class.java)
}