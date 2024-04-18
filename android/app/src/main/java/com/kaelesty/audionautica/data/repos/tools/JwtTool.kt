package com.kaelesty.audionautica.data.repos.tools

import android.util.Log
import com.kaelesty.audionautica.data.local.daos.TokenDao
import com.kaelesty.audionautica.data.local.dbmodels.TokenDbModel
import javax.inject.Inject

class JwtTool @Inject constructor(
	private val tokenDao: TokenDao,
) {

	fun saveToken(jwt: String) {
		Log.d("AudionauticaTag", "Saved: $jwt")
		tokenDao.saveToken(
			TokenDbModel(0, jwt)
		)
	}

	fun delToken() {
		tokenDao.dropToken()
	}

	fun getToken(): String {
		val jwt = tokenDao.getToken().jwt
		Log.d("AudionauticaTag", "Loaded: $jwt")
		return jwt
	}
}