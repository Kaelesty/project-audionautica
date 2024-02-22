package com.kaelesty.audionautica.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceFactory {

	private const val SERVER_URL = "https://83cb-188-244-132-51.ngrok-free.app"


	private const val ACCESS_URL = "$SERVER_URL/Auth/"
	private const val MUSIC_URL = "$SERVER_URL/Music/"

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