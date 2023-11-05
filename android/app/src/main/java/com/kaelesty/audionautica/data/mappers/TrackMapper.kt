package com.kaelesty.audionautica.data.mappers

import com.kaelesty.audionautica.data.local.dbmodels.TrackDbModel
import com.kaelesty.audionautica.domain.entities.Track
import javax.inject.Inject

class TrackMapper @Inject constructor() {

	fun dbModelToDomain(dbModel: TrackDbModel): Track = Track(
		dbModel.id,
		dbModel.title,
		dbModel.artist,
		dbModel.duration,
		dbModel.musicFile,
		dbModel.posterFile,
	)

	fun domainToDbModel(domain: Track): TrackDbModel = TrackDbModel(
		domain.id,
		domain.title,
		domain.artist,
		domain.duration,
		domain.musicFile,
		domain.posterFile,
	)
}