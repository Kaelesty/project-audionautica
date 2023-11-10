package com.kaelesty.audionautica.data.mappers

import android.net.Uri
import androidx.core.net.toUri
import com.kaelesty.audionautica.data.local.dbmodels.TrackDbModel
import com.kaelesty.audionautica.domain.entities.Track
import java.io.File
import javax.inject.Inject

class TrackMapper @Inject constructor() {

	fun dbModelToDomain(dbModel: TrackDbModel): Track = Track(
		dbModel.id,
		dbModel.title,
		dbModel.artist,
		dbModel.duration,
		File(dbModel.musicFile),
		dbModel.posterFile?.let {
								Uri.fromFile(File(it))
		},
	)

	fun domainToDbModel(domain: Track): TrackDbModel = TrackDbModel(
		domain.id,
		domain.title,
		domain.artist,
		domain.duration,
		domain.musicFile.path ?: "",
		domain.posterFile?.path ?: "",
	)
}