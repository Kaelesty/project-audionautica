package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IAccessRepo

class CheckConnectionUseCase(
	private val repo: IAccessRepo
) {

	suspend operator fun invoke() = repo.checkConnection()
}