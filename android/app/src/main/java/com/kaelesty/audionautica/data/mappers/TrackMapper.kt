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

	val TRACK_TAGS_STRINGIFICATION_DELIMITER = "%"

	fun dbModelToDomain(dbModel: TrackDbModel): Track = Track(
		dbModel.id,
		dbModel.title,
		dbModel.artist,
		tags = dbModel.tags.split(TRACK_TAGS_STRINGIFICATION_DELIMITER),
	)

	fun domainToDbModel(domain: Track): TrackDbModel = TrackDbModel(
		domain.id,
		domain.title,
		domain.artist,
		tags = domain.tags.joinToString(TRACK_TAGS_STRINGIFICATION_DELIMITER)
	)

	fun dtoToDomain(dto: TrackDto) = Track(
		dto.id,
		dto.title,
		dto.artist,
		tags = dto.tags.split(TRACK_TAGS_STRINGIFICATION_DELIMITER),
	)
}