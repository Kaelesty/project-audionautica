package com.kaelesty.audionautica.domain.playlists

import javax.inject.Inject

class SearchPlaylistsUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	suspend operator fun invoke(query: String) = repo.searchPlaylists(query)
}