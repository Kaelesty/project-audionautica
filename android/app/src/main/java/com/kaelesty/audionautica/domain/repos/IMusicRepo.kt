package com.kaelesty.audionautica.domain.repos

import android.net.Uri
import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackExp
import com.kaelesty.audionautica.domain.entities.TracksToPlay
import com.kaelesty.audionautica.domain.returncodes.UploadTrackRC
import kotlinx.coroutines.flow.SharedFlow

interface IMusicRepo {

	fun getTracks(): LiveData<List<Track>>

	suspend fun addTrack(track: TrackExp)

	suspend fun uploadTrack(track: TrackExp): UploadTrackRC

	suspend fun search(query: String): List<Track>

	suspend fun addToTracksQueue(
		track: List<Track>,
		dropQueue: Boolean,
	)

	suspend fun getTrackUri(id: Int): Uri

	fun getTracksQueueFlow(): SharedFlow<TracksToPlay>
}