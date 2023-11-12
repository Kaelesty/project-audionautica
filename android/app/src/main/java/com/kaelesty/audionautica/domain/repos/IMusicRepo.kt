package com.kaelesty.audionautica.domain.repos

import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackExp
import com.kaelesty.audionautica.domain.returncodes.UploadTrackRC

interface IMusicRepo {

	fun getTracks(): LiveData<List<Track>>

	suspend fun addTrack(track: TrackExp)

	suspend fun uploadTrack(track: TrackExp): UploadTrackRC

	suspend fun search(query: String): List<Track>
}