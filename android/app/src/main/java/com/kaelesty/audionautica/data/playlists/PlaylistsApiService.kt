package com.kaelesty.audionautica.data.playlists

import com.kaelesty.audionautica.di.ApplicationScope
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

@ApplicationScope
interface PlaylistsApiService {

	@Multipart
	@POST("Upload/")
	suspend fun uploadPlaylist(
		@Header("title") title: String,
		@Header("creatorid") creatorId: Int,
		@Header("description") desc: String,
		@Header("token") token: String,
		@Header("tracksid") trackIds: String,
		@Header("isprivat") isPrivate: Boolean,
		@Header("ext") ext: String,
		@Part posterFile: MultipartBody.Part,
	): Response<ResponseBody>
}