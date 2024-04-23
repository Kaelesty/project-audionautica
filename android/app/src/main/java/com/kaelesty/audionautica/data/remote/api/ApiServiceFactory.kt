package com.kaelesty.audionautica.data.remote.api

import com.kaelesty.audionautica.data.playlists.PlaylistsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiServiceFactory {

	private const val PROTOCOL = "https://"
	private const val SERVER_URL = "0d4e-188-244-132-93.ngrok-free.app"

	const val ACCESS_URL = "$PROTOCOL$SERVER_URL/Auth/"
	const val MUSIC_URL =  "$PROTOCOL$SERVER_URL/Music/"
	const val PLAYLIST_URL = "$PROTOCOL$SERVER_URL/PlayList/"

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

	val playlistsService: PlaylistsApiService = Retrofit.Builder()
		.baseUrl(PLAYLIST_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(PlaylistsApiService::class.java)
}