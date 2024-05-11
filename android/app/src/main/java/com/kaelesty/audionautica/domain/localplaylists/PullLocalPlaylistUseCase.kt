package com.kaelesty.audionautica.domain.localplaylists

import javax.inject.Inject

class PullLocalPlaylistUseCase @Inject constructor(
	private val repo: ILocalPlaylistsRepo
) {

	suspend operator fun invoke(playlist: LocalPlaylist) = repo.pullLocalPlaylist(playlist)
}