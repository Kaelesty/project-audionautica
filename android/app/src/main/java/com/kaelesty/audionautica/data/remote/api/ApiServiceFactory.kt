package com.kaelesty.audionautica.data.remote.api

import com.kaelesty.audionautica.data.playlists.PlaylistsApiService
import com.kaelesty.audionautica.data.reco.RecoApiService
import com.kaelesty.audionautica.data.reco.RecoRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiServiceFactory {

	private const val PROTOCOL = "https://"
	private const val SERVER_URL = "1697-193-32-202-60.ngrok-free.app"

	const val ACCESS_URL = "$PROTOCOL$SERVER_URL/Auth/"
	const val MUSIC_URL =  "$PROTOCOL$SERVER_URL/Music/"
	const val PLAYLIST_URL = "$PROTOCOL$SERVER_URL/PlayList/"
	const val RECO_URL = "$PROTOCOL$SERVER_URL/Reco/"

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

	val recoService: RecoApiService = Retrofit.Builder()
		.baseUrl(RECO_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(RecoApiService::class.java)
}