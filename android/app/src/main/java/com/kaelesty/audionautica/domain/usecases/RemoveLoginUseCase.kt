package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IAccessRepo
import javax.inject.Inject

class RemoveLoginUseCase @Inject constructor(
	val repo: IAccessRepo
) {

	suspend operator fun invoke(login: String) = repo.removeLogin(login)
}