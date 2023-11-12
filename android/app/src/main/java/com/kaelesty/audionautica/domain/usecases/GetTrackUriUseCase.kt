package com.kaelesty.audionautica.domain.usecases

import android.net.Uri
import com.kaelesty.audionautica.data.repos.MusicRepo
import javax.inject.Inject

class GetTrackUriUseCase @Inject constructor(
	private val repo: MusicRepo
) {

	suspend operator fun invoke(id: Int): Uri {
		TODO("""
			This usecase should return the Uri of a track by ID for use in ExoPlayer. 
			If the track has already been downloaded, just take the path from the database, 
			if not, then download and return the path
		""".trimIndent())
	}
}