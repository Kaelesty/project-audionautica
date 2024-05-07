package com.kaelesty.audionautica.domain.localplaylists

import javax.inject.Inject

class SaveLocalPlaylistUseCase @Inject constructor(
	private val repo: ILocalPlaylistsRepo
) {

	suspend operator fun invoke(playlist: LocalPlaylist) = repo.saveLocalPlaylist(playlist)
}