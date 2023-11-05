package com.kaelesty.audionautica.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.mappers.TrackMapper
import com.kaelesty.audionautica.di.ApplicationScope
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

@ApplicationScope
class MusicRepo @Inject constructor(
	private val trackDao: TrackDao,
	private val playlistDao: PlaylistDao,
	private val trackMapper: TrackMapper,
): IMusicRepo {


	override suspend fun logout() {
		// TODO delete JWT
	}

	override fun getTracks(): LiveData<List<Track>> {
		return trackDao.getAll().map { it.map { dbModel -> trackMapper.dbModelToDomain(dbModel) } }
	}

	override suspend fun addTrack(track: Track) {
		// TODO upload to Server Database
		trackDao.createTrack(trackMapper.domainToDbModel(track))
	}
}