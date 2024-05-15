package com.kaelesty.audionautica.domain.playlists

import javax.inject.Inject

class LoadCachedPlaylistUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	suspend operator fun invoke(id: Int, update: Boolean = false) = repo.loadCachedPlaylist(id, update)
}