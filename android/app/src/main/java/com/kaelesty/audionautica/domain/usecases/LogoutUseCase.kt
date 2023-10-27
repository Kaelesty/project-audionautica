package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IMusicRepo

class LogoutUseCase(private val repo: IMusicRepo) {

	suspend operator fun invoke() = repo.logout()
}