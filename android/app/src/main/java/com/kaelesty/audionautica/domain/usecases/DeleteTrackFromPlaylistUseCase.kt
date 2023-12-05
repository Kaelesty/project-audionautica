package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

class DeleteTrackFromPlaylistUseCase @Inject constructor(
	private val repo: IMusicRepo
) {

	suspend operator fun invoke(
		track: Track,
		playlistId: Int
	) = repo.removeTrackFromPlaylist(
		track, playlistId,
	)
}