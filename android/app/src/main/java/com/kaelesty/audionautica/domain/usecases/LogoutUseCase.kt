package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

class LogoutUseCase @Inject constructor(val repo: IMusicRepo) {

	suspend operator fun invoke() = repo.logout()
}