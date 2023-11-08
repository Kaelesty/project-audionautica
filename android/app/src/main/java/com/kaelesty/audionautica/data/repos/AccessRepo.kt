package com.kaelesty.audionautica.data.repos

import com.kaelesty.audionautica.data.remote.api.AccessApiService
import com.kaelesty.audionautica.data.remote.entities.LoginDto
import com.kaelesty.audionautica.data.remote.entities.RegisterDto
import com.kaelesty.audionautica.di.ApplicationScope
import com.kaelesty.audionautica.domain.repos.IAccessRepo
import com.kaelesty.audionautica.domain.returncodes.CheckConnectionRC
import com.kaelesty.audionautica.domain.returncodes.LoginRC
import com.kaelesty.audionautica.domain.returncodes.RegisterRC
import javax.inject.Inject

@ApplicationScope
class AccessRepo @Inject constructor(
	val accessApiService: AccessApiService
) : IAccessRepo {


	override suspend fun login(email: String, password: String): LoginRC {
		try {
			val response = accessApiService.login(
				LoginDto(email, password)
			)
			return when (response.code()) {
				200 -> {
					// TODO save user's jwt to database and etc...
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
			return LoginRC.UNKNOWN
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