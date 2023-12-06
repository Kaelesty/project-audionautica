package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.data.repos.MusicRepo
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

class PlayPrevUseCase @Inject constructor(
	private val repo: IMusicRepo
) {

	suspend operator fun invoke() = repo.playPrev()
}