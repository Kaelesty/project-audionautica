package com.kaelesty.audionautica.domain.usecases

import android.net.Uri
import com.kaelesty.audionautica.data.repos.MusicRepo
import javax.inject.Inject

class GetTrackUriUseCase @Inject constructor(
	private val repo: MusicRepo
) {

	suspend operator fun invoke(id: Int) = repo.getTrackUri(id)
}