package com.kaelesty.audionautica.domain.playlists

import javax.inject.Inject

class UpdatePlaylistTracksetUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	suspend operator fun invoke(id: Int, trackIds: List<Int>) = repo.updatePlaylistTrackset(id, trackIds)
}