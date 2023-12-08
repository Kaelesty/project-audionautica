package com.kaelesty.audionautica.domain.repos

import com.kaelesty.audionautica.domain.returncodes.CheckAuthRC
import com.kaelesty.audionautica.domain.returncodes.CheckConnectionRC
import com.kaelesty.audionautica.domain.returncodes.LoginRC
import com.kaelesty.audionautica.domain.returncodes.RegisterRC

interface IAccessRepo {

	suspend fun login(email: String, password: String): LoginRC

	suspend fun register(email: String, name: String, password: String): RegisterRC

	suspend fun checkConnection(): CheckConnectionRC

	suspend fun checkAuth(): CheckAuthRC

	suspend fun logout() // no return because we just del token from datastore. nothing can go wrong ;)
}