package com.kaelesty.audionautica.domain.playlists

import javax.inject.Inject

class UpdatePlaylistCollectionUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	suspend operator fun invoke() = repo.updatePlaylistCollection()
}