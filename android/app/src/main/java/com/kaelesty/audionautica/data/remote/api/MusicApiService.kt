package com.kaelesty.audionautica.data.remote.api

import com.kaelesty.audionautica.data.remote.entities.DownloadTrackDto
import com.kaelesty.audionautica.data.remote.entities.SearchDto
import com.kaelesty.audionautica.data.remote.entities.TracksSearchResponse
import com.kaelesty.audionautica.di.ApplicationScope
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Streaming

@ApplicationScope
interface MusicApiService {

	@Streaming
	@POST("GetTrack/")
	suspend fun downloadTrackSample(
		@Header("token") token: String,
		@Body body: DownloadTrackDto
	): Response<ResponseBody>

	@POST("Search/")
	suspend fun searchTracks(
		@Header("token") token: String,
		@Body body: SearchDto
	): Response<TracksSearchResponse>

	@Multipart
	@POST("Upload/")
	suspend fun uploadTrack(
		@Header("title") title: String,
		@Header("artist") artist: String,
		@Header("tags") tags: String,
		@Header("token") token: String,
		@Part("description") description: RequestBody,
		@Part musicFile: MultipartBody.Part,
	): Response<ResponseBody>


}