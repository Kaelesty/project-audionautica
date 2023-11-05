package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IAccessRepo
import javax.inject.Inject

class LoginUseCase @Inject constructor(val repo: IAccessRepo) {

	suspend operator fun invoke(email: String, password: String) = repo.login(email, password)
}