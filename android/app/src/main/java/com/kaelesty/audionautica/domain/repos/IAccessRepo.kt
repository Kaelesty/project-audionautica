package com.kaelesty.audionautica.domain.repos

import com.kaelesty.audionautica.domain.returncodes.CheckAuthRC
import com.kaelesty.audionautica.domain.returncodes.CheckConnectionRC
import com.kaelesty.audionautica.domain.returncodes.LoginRC
import com.kaelesty.audionautica.domain.returncodes.RegisterRC
import kotlinx.coroutines.flow.SharedFlow

interface IAccessRepo {

	suspend fun login(email: String, password: String): LoginRC

	suspend fun register(email: String, name: String, password: String): RegisterRC

	suspend fun checkConnection(): CheckConnectionRC

	fun getUserEmail(): String?

	suspend fun checkAuth(): CheckAuthRC

	suspend fun logout()

	suspend fun getLogoutFlow(): SharedFlow<Unit>

	suspend fun getLogins(): List<String>

	suspend fun getPassword(login: String): String

	suspend fun removeLogin(login: String)

	suspend fun saveLogin(login: String, password: String)

	suspend fun getUserInfo(): Pair<String, String>?
}