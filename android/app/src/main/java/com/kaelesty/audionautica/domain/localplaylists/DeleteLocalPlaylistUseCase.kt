package com.kaelesty.audionautica.domain.localplaylists

import javax.inject.Inject

class DeleteLocalPlaylistUseCase @Inject constructor(
	private val repo: ILocalPlaylistsRepo
) {

	suspend operator fun invoke(playlistId: Int) = repo.deleteLocalPlaylist(playlistId)
}