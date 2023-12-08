package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.data.repos.MusicRepo
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

class DeletePlaylistUseCase @Inject constructor(
	private val repo: IMusicRepo
) {

	suspend operator fun invoke(
		playlistId: Int
	) = repo.deletePlaylist(
		playlistId
	)
}