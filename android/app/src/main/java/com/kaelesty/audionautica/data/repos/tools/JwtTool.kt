package com.kaelesty.audionautica.data.repos.tools

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kaelesty.audionautica.data.local.daos.TokenDao
import com.kaelesty.audionautica.data.local.dbmodels.TokenDbModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JwtTool @Inject constructor(
	private val application: Application,
	private val tokenDao: TokenDao,
) {

	val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
	private val DATASTORE_JWT_KEY = stringPreferencesKey("JWT")

	suspend fun saveToken(jwt: String) {
		Log.d("AudionauticaTag", "Saved: $jwt")
		tokenDao.saveToken(
			TokenDbModel(0, jwt)
		)
	}

	suspend fun delToken() {
		tokenDao.dropToken()
	}

	fun getToken(): String {
		val jwt = tokenDao.getToken().jwt
		Log.d("AudionauticaTag", "Loaded: $jwt")
		return jwt
	}
}