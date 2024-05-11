package com.kaelesty.audionautica.domain.playlists

import javax.inject.Inject

class LoadLiveEntirePlaylistUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	operator fun invoke(id: Int) = repo.loadLiveEntirePlaylist(id)
}