package com.kaelesty.audionautica.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceFactory {

	private const val PROTOCOL = "https://"
	private const val SERVER_URL = "6b3c-193-32-202-60.ngrok-free.app"

	private const val ACCESS_URL = "$PROTOCOL$SERVER_URL/Auth/"
	private const val MUSIC_URL =  "$PROTOCOL$SERVER_URL/Music/"

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