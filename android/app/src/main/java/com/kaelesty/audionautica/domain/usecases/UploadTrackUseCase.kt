package com.kaelesty.audionautica.domain.usecases

import android.net.Uri
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackExp
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

class UploadTrackUseCase @Inject constructor(
	private val repo: IMusicRepo
) {
	suspend operator fun invoke(track: Track, uri: Uri) = repo.uploadTrack(track, uri)
}