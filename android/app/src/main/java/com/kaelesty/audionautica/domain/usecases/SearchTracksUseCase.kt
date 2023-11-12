package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor(
	private val repo: IMusicRepo
) {
	suspend operator fun invoke(query: String) = repo.search(query)
}