package com.kaelesty.audionautica.domain.playlists

import javax.inject.Inject

class GetPlaylistCollectionUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	operator fun invoke() = repo.getPlaylistCollection()
}