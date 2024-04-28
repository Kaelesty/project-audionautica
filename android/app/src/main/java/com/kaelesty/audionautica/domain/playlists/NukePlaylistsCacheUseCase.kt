package com.kaelesty.audionautica.domain.playlists

import javax.inject.Inject

class NukePlaylistsCacheUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	suspend operator fun invoke() = repo.nukeTable()
}