package com.kaelesty.audionautica.domain.localplaylists

import javax.inject.Inject

class GetPlaylistsUseCase @Inject constructor(
	private val repo: ILocalPlaylistsRepo
) {

	operator fun invoke() = repo.getLocalPlaylists()
}