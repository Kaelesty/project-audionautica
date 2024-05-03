package com.kaelesty.audionautica.domain.playlists

import javax.inject.Inject

class AddTrackToPlaylistUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	suspend operator fun invoke(trackId: Int, playlistId: Int) = repo.addTrackToPlaylist(trackId, playlistId)
}