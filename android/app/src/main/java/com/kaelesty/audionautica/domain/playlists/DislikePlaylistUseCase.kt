package com.kaelesty.audionautica.domain.playlists

import javax.inject.Inject

class DislikePlaylistUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	suspend operator fun invoke(id: Int) = repo.dislikePlaylist(id)
}