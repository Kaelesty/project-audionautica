package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.entities.TrackExp
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

class CreateTrackUseCase @Inject constructor(
	private val repo: IMusicRepo
) {
	suspend operator fun invoke(track: TrackExp) {
		repo.addTrack(track)
	}
}