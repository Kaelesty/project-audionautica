package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IAccessRepo
import javax.inject.Inject

class GetPasswordUseCase @Inject constructor(
	val repo: IAccessRepo
) {

	suspend operator fun invoke(login: String) = repo.getPassword(login)
}