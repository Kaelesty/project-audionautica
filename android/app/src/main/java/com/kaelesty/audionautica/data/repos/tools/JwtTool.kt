package com.kaelesty.audionautica.data.repos.tools

import android.util.Log
import com.kaelesty.audionautica.data.local.daos.AccessDao
import com.kaelesty.audionautica.data.local.dbmodels.TokenDbModel
import javax.inject.Inject

class JwtTool @Inject constructor(
	private val accessDao: AccessDao,
) {

	fun saveToken(jwt: String) {
		Log.d("AudionauticaTag", "Saved: $jwt")
		accessDao.saveToken(
			TokenDbModel(0, jwt)
		)
	}

	fun delToken() {
		accessDao.dropToken()
	}

	fun getToken(): String {
		val jwt = accessDao.getToken().jwt
		Log.d("AudionauticaTag", "Loaded: $jwt")
		return jwt
	}
}