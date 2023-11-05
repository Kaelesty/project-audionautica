package com.kaelesty.audionautica.domain.repos

import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Track

interface IMusicRepo {

	suspend fun logout()

	fun getTracks(): LiveData<List<Track>>

	suspend fun addTrack(track: Track)
}