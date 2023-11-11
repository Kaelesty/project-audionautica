package com.kaelesty.audionautica.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceFactory {

	private const val ACCESS_URL = "https://15a6-193-32-202-124.ngrok-free.app" + "/Auth/"
	private const val MUSIC_URL = " https://15a6-193-32-202-124.ngrok-free.app" + "/Music/"

	val accessService: AccessApiService = Retrofit.Builder()
		.baseUrl(ACCESS_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(AccessApiService::class.java)

	val musicService: MusicApiService = Retrofit.Builder()
		.baseUrl(MUSIC_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(MusicApiService::class.java)
}