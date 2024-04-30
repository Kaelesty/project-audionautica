package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IAccessRepo
import javax.inject.Inject

class SaveLoginUseCase @Inject constructor(
	val repo: IAccessRepo
) {

	suspend operator fun invoke(login: String, password: String) = repo.saveLogin(login, password)
}