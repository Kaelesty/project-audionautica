package com.kaelesty.audionautica.data.mappers

import com.kaelesty.audionautica.data.local.dbmodels.PlaylistDbModel
import com.kaelesty.audionautica.domain.playlists.Playlist
import javax.inject.Inject

class PlaylistMapper @Inject constructor() {

	val TRACK_IDS_STRINGIFICATION_DELIMITER = "$"

//	fun DbModelToDomain(dbModel: PlaylistDbModel) = Playlist(
//		id = dbModel.id ?: -1,
//		title = dbModel.title,
//		trackIds = dbModel.trackIds
//	)
//
//	fun DomainToDbModel(domain: Playlist) = PlaylistDbModel(
//		id = if (domain.id == -1) null else domain.id,
//		title = domain.title,
//		trackIds = domain.trackIds
//	)
}