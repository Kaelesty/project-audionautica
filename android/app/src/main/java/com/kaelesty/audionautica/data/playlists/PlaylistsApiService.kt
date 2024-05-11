package com.kaelesty.audionautica.data.playlists

import com.kaelesty.audionautica.data.remote.entities.SearchDto
import com.kaelesty.audionautica.di.ApplicationScope
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
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
		@Header("description") desc: String,
		@Header("token") token: String,
		@Header("tracksid") trackIds: String,
		@Header("isprivat") isPrivate: Boolean,
		@Header("ext") ext: String,
		@Part posterFile: MultipartBody.Part,
	): Response<ResponseBody>

	@POST("LikePlayList/")
	suspend fun likePlaylist(
		@Header("token") token: String,
		@Body body: PlaylistIdRequest
	): Response<ResponseBody>

	@POST("UserPlayList/")
	suspend fun getPlaylists(
		@Header("token") token: String,
	): Response<UserPlaylistResponse>

	@POST("PlayListInfoId/")
	suspend fun playlistInfoId(
		@Header("token") token: String,
		@Body body: EntirePlaylistRequest
	): Response<EntirePlaylistResponse>

	@POST("AddTrackToPlayList/")
	suspend fun addTrackToPlayList(
		@Header("token") token: String,
		@Body body: AddTrackToPlaylistRequest
	): Response<ResponseBody>

	@POST("Search/")
	suspend fun searchPlaylists(
		@Header("token") token: String,
		@Body body: SearchDto
	): Response<PlaylistsSearchResponse>

	@POST("UpdatePlaylistTrackset/")
	suspend fun updatePlaylistTrackset(
		@Header("token") token: String,
		@Body body: UpdatePlaylistTracksetRequest
	): Response<ResponseBody>
}