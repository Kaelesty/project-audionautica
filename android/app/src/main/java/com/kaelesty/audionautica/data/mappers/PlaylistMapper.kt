package com.kaelesty.audionautica.data.mappers

import android.net.Uri
import androidx.core.net.toUri
import com.kaelesty.audionautica.data.local.dbmodels.PlaylistDbModel
import com.kaelesty.audionautica.data.local.dbmodels.TrackDbModel
import com.kaelesty.audionautica.data.remote.entities.TrackDto
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackExp
import java.io.File
import javax.inject.Inject

class PlaylistMapper @Inject constructor() {

	val TRACK_IDS_STRINGIFICATION_DELIMITER = "$"

	fun DbModelToDomain(dbModel: PlaylistDbModel) = Playlist(
		id = dbModel.id ?: -1,
		title = dbModel.title,
		trackIds = dbModel.trackIds
	)

	fun DomainToDbModel(domain: Playlist) = PlaylistDbModel(
		id = if (domain.id == -1) null else domain.id,
		title = domain.title,
		trackIds = domain.trackIds
	)
}