package com.kaelesty.audionautica.data.repos.tools

import com.kaelesty.audionautica.data.local.daos.AccessDao
import com.kaelesty.audionautica.data.remote.api.ApiServiceFactory
import javax.inject.Inject

class UrlTool @Inject constructor(
	private val accessDao: AccessDao,
) {

	fun getPlaylistPosterUrl(
		id: Int,
		token: String
	) = "${ApiServiceFactory.PLAYLIST_URL}GetPoster/?id=$id&token=$token"
}