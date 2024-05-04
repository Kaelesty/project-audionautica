package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IAccessRepo
import javax.inject.Inject

class GetUserEmailUseCase @Inject constructor(
	val repo: IAccessRepo
) {

	operator fun invoke() = repo.getUserEmail()
}