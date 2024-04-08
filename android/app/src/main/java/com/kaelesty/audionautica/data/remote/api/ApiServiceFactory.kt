package com.kaelesty.audionautica.data.remote.api

import com.kaelesty.audionautica.data.playlists.PlaylistsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceFactory {

	private const val PROTOCOL = "https://"
	private const val SERVER_URL = "cb74-193-32-202-60.ngrok-free.app"

	private const val ACCESS_URL = "$PROTOCOL$SERVER_URL/Auth/"
	private const val MUSIC_URL =  "$PROTOCOL$SERVER_URL/Music/"
	private const val PLAYLIST_URL = "$PROTOCOL$SERVER_URL/PlayList/"

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