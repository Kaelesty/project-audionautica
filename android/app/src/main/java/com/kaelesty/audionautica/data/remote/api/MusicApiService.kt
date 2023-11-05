package com.kaelesty.audionautica.data.remote.api

import com.kaelesty.audionautica.data.remote.entities.LoginDto
import com.kaelesty.audionautica.data.remote.entities.LoginResponse
import com.kaelesty.audionautica.data.remote.entities.RegisterDto
import com.kaelesty.audionautica.data.remote.entities.RegisterResponse
import com.kaelesty.audionautica.di.ApplicationScope
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

@ApplicationScope
interface MusicApiService {

	@Streaming
	@GET("GetTrack")
	suspend fun downloadTrackSample(): Response<ResponseBody>
	//@Url fileUrl:String
}