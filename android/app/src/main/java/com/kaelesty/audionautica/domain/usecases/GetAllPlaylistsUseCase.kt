package com.kaelesty.audionautica.domain.usecases

import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

class GetAllPlaylistsUseCase @Inject constructor(
	private val repo: IMusicRepo
) {
	operator fun invoke() = repo.getPlaylists()
}