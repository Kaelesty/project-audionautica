package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.data.repos.MusicRepo
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

class GetPlayingTrackFlowUseCase @Inject constructor(
	private val repo: IMusicRepo
) {

	operator fun invoke(
	) = repo.getPlayingTrackFlow()
}