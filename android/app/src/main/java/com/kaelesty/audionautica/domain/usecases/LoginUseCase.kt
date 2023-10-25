package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IAccessRepo

class LoginUseCase(private val repo: IAccessRepo) {

	suspend operator fun invoke(email: String, password: String) = repo.login(email, password)
}