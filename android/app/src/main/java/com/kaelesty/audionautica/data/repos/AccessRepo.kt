package com.kaelesty.audionautica.data.repos

import com.kaelesty.audionautica.data.remote.api.AccessServiceFactory
import com.kaelesty.audionautica.data.remote.entities.LoginDto
import com.kaelesty.audionautica.data.remote.entities.RegisterDto
import com.kaelesty.audionautica.domain.repos.IAccessRepo
import com.kaelesty.audionautica.domain.returncodes.LoginRC
import com.kaelesty.audionautica.domain.returncodes.RegisterRC

class AccessRepo: IAccessRepo {

	private val accessApiService by lazy {
		AccessServiceFactory.apiService
	}

	override suspend fun login(email: String, password: String): LoginRC {
		val response = accessApiService.login(
			LoginDto(email, password)
		)

		return when (response.code()) {
			200 -> {
				// TODO save user's jwt to database and etc...
				LoginRC.OK
			}

			else -> {
				LoginRC.BAD_REQUEST
			}
		}
	}

	override suspend fun register(email: String, name: String, password: String): RegisterRC {
		val response = accessApiService.register(
			RegisterDto(email, name, password)
		)

		return when (response.code()) {
			200 -> {
				// TODO save user's jwt to database and etc...
				RegisterRC.OK
			}

			else -> {
				RegisterRC.BAD_REQUEST
			}
		}
	}
}