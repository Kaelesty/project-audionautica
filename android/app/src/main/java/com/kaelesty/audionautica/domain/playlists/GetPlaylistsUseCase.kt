package com.kaelesty.audionautica.domain.playlists

import javax.inject.Inject

class GetPlaylistsUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	suspend operator fun invoke() = repo.getPlaylists()
}