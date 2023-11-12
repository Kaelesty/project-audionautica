package com.kaelesty.audionautica.data.mappers

import android.net.Uri
import androidx.core.net.toUri
import com.kaelesty.audionautica.data.local.dbmodels.TrackDbModel
import com.kaelesty.audionautica.data.remote.entities.TrackDto
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackExp
import java.io.File
import javax.inject.Inject

class TrackMapper @Inject constructor() {

	fun dbModelToDomain(dbModel: TrackDbModel): Track = Track(
		dbModel.id,
		dbModel.title,
		dbModel.artist,
		dbModel.poster,
	)

	fun domainToDbModel(domain: Track): TrackDbModel = TrackDbModel(
		domain.id,
		domain.title,
		domain.artist,
		domain.poster
	)

	fun dtoToDomain(dto: TrackDto) = Track(
		dto.id,
		dto.title,
		dto.artist,
		dto.poster,
	)
}