package com.kaelesty.audionautica.data.repos.tools

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JwtTool @Inject constructor(
	private val application: Application
) {

	val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
	private val DATASTORE_JWT_KEY = stringPreferencesKey("JWT")

	suspend fun saveToken(jwt: String) {
		application.dataStore.edit { settings ->
			settings[DATASTORE_JWT_KEY] = jwt
		}
	}

	suspend fun delToken() {
		application.dataStore.edit { settings ->
			settings[DATASTORE_JWT_KEY] = ""
		}
	}

	fun getToken(): String {
		var token: String = ""
		application.dataStore.data.map { preferences ->
			token = preferences[DATASTORE_JWT_KEY] ?: ""
		}
		return token
	}
}