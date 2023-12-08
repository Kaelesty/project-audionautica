package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IAccessRepo
import javax.inject.Inject

class CheckAuthUseCase @Inject constructor(
	val repo: IAccessRepo
) {

	suspend operator fun invoke() = repo.checkAuth()
}