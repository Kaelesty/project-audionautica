package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.entities.TrackExp
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

class UploadTrackUseCase @Inject constructor(
	private val repo: IMusicRepo
) {
	suspend operator fun invoke(track: TrackExp) = repo.uploadTrack(track)
}