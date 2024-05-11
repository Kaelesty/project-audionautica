package com.kaelesty.audionautica.domain.localplaylists

import javax.inject.Inject

class PushLocalPlaylistUseCase @Inject constructor(
	private val repo: ILocalPlaylistsRepo
) {

	suspend operator fun invoke(playlist: LocalPlaylist) = repo.pushLocalPlaylist(playlist)
}