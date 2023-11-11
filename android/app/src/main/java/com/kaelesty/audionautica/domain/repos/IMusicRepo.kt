package com.kaelesty.audionautica.domain.repos

import android.net.Uri
import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.returncodes.UploadTrackRC

interface IMusicRepo {

	fun getTracks(): LiveData<List<Track>>

	suspend fun addTrack(track: Track)

	suspend fun uploadTrack(track: Track): UploadTrackRC
}