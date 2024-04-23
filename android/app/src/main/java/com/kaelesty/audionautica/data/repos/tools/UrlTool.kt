package com.kaelesty.audionautica.data.repos.tools

import android.util.Log
import androidx.datastore.preferences.protobuf.Api
import com.kaelesty.audionautica.data.local.daos.TokenDao
import com.kaelesty.audionautica.data.local.dbmodels.TokenDbModel
import com.kaelesty.audionautica.data.remote.api.ApiServiceFactory
import javax.inject.Inject

class UrlTool @Inject constructor(
	private val tokenDao: TokenDao,
) {

	fun getPlaylistPosterUrl(
		id: Int,
		token: String
	) = "${ApiServiceFactory.PLAYLIST_URL}GetPoster/?id=$id&token=$token"
}