package com.kaelesty.audionautica.data.repos

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kaelesty.audionautica.data.remote.api.AccessApiService
import com.kaelesty.audionautica.data.remote.entities.JwtDto
import com.kaelesty.audionautica.data.remote.entities.LoginDto
import com.kaelesty.audionautica.data.remote.entities.RegisterDto
import com.kaelesty.audionautica.data.repos.tools.JwtTool
import com.kaelesty.audionautica.di.ApplicationScope
import com.kaelesty.audionautica.domain.repos.IAccessRepo
import com.kaelesty.audionautica.domain.returncodes.CheckAuthRC
import com.kaelesty.audionautica.domain.returncodes.CheckConnectionRC
import com.kaelesty.audionautica.domain.returncodes.LoginRC
import com.kaelesty.audionautica.domain.returncodes.RegisterRC
import javax.inject.Inject

@ApplicationScope
class AccessRepo @Inject constructor(
	private val accessApiService: AccessApiService,
	private val jwtTool: JwtTool
) : IAccessRepo {


	override suspend fun login(email: String, password: String): LoginRC {
		try {
			val response = accessApiService.login(
				LoginDto(email, password)
			)
			return when (response.code()) {
				200 -> {
					val jwt = response.body() ?: throw IllegalStateException("EMPTY JWT")
					jwtTool.saveToken(jwt)
					LoginRC.OK
				}

				400 -> {
					LoginRC.BAD_LOGIN
				}

				401 -> {
					LoginRC.BAD_PASSWORD
				}

				else -> {
					LoginRC.UNKNOWN
				}
			}
		}
		catch (exception: Exception) {
			Log.d("AudionauticaTag", exception.message.toString())
			return LoginRC.UNKNOWN
		}
	}

	override suspend fun logout() {
		jwtTool.delToken()
	}

	override suspend fun checkAuth(): CheckAuthRC {
		try {
			val token = jwtTool.getToken()
			val response = accessApiService.checkAuth(
				token
			)
			if (response.code() == 200) {
				return CheckAuthRC.OK
			}
			return CheckAuthRC.NOT_OK
		} catch (exception: Exception) {
			Log.d("Audionautica tag", exception.message.toString())
			return CheckAuthRC.NOT_OK
		}
	}

	override suspend fun register(email: String, name: String, password: String): RegisterRC {
		try {
			val response = accessApiService.register(
				RegisterDto(email, name, password)
			)
			return when (response.code()) {
				200 -> autoLogin(email, password)
				400 -> RegisterRC.BAD_EMAIL
				else -> RegisterRC.UNKNOWN
			}
		}
		catch (exception: Exception) {
			return RegisterRC.UNKNOWN
		}
	}

	override suspend fun checkConnection(): CheckConnectionRC {
		return try {
			val response = accessApiService.checkConnection()
			when (response.code()) {
				200 -> CheckConnectionRC.OK
				else -> CheckConnectionRC.NOT_OK
			}
		} catch (exception: Exception) {
			CheckConnectionRC.NOT_OK
		}
	}

	private suspend fun autoLogin(email: String, password: String): RegisterRC {
		return when (login(email, password)) {
			LoginRC.OK -> RegisterRC.OK
			LoginRC.BAD_PASSWORD, LoginRC.BAD_LOGIN -> RegisterRC.AUTOLOGIN_FATAL_ERROR
			LoginRC.UNKNOWN -> RegisterRC.AUTOLOGIN_FAILED
		}
	}

}