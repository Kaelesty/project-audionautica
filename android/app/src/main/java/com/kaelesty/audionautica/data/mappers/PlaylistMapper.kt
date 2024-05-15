package com.kaelesty.audionautica.data.mappers


import com.kaelesty.audionautica.data.local.dbmodels.PlaylistCacheDbModel
import com.kaelesty.audionautica.data.playlists.EntirePlaylistResponse
import com.kaelesty.audionautica.data.playlists.IdEntirePlaylistResponse
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import javax.inject.Inject

class PlaylistMapper @Inject constructor() {

	val TRACK_IDS_STRINGIFICATION_DELIMITER = "/"

	fun DbModelToDomain(dbModel: PlaylistCacheDbModel): UserPlaylist.Entire? {
		return UserPlaylist.Entire(
			id = dbModel.id,
			title = dbModel.title ?: return null,
			desc = dbModel.desc ?: return null,
			isEditable = dbModel.isEditable ?: return null,
			isPrivate = dbModel.isPrivate ?: return null,
			imageUrl = dbModel.imageUrl ?: return null,
			trackIds = dbModel.trackIds ?: return null,
			likes = dbModel.likes ?: return null,
			inCollection = dbModel.inUserCollection ?: return null
		)
	}

	fun domainToDbModel(domain: UserPlaylist.Entire): PlaylistCacheDbModel {
		return PlaylistCacheDbModel(
			id = domain.id,
			likes = domain.likes,
			trackIds = domain.trackIds,
			desc = domain.desc,
			title = domain.title,
			imageUrl = domain.imageUrl,
			isPrivate = domain.isPrivate,
			isEditable = domain.isEditable,
			inUserCollection = domain.inCollection
		)
	}

	fun domainToDbModel(domain: UserPlaylist.IdOnly): PlaylistCacheDbModel {
		return PlaylistCacheDbModel(
			id = domain.id,
			likes = null,
			trackIds = null,
			desc = null,
			title = null,
			imageUrl = null,
			isPrivate = null,
			isEditable = null,
			inUserCollection = null
		)
	}

	fun ResponseToDomain(
		id: Int,
		res: EntirePlaylistResponse,
		imageUrl: String,
	) = UserPlaylist.Entire(
		id = id,
		title = res.title,
		desc = res.description,
		isEditable = res.isEditable,
		isPrivate = res.isPrivate,
		trackIds = trackIdsStringToList(res.trackIds),
		likes = res.likes,
		imageUrl = imageUrl,
		inCollection = res.inCollection
	)

	fun IdResponseToDomain(
		res: IdEntirePlaylistResponse,
		imageUrl: String,
		inCollection: Boolean
	) = UserPlaylist.Entire(
		id = res.id,
		title = res.title,
		desc = res.description,
		isEditable = res.isEditable,
		isPrivate = res.isPrivate,
		trackIds = trackIdsStringToList(res.trackIds),
		likes = res.likes,
		imageUrl = imageUrl,
		inCollection = inCollection
	)

	fun trackIdsStringToList(trackIds: String) = trackIds.split(TRACK_IDS_STRINGIFICATION_DELIMITER)
		.filter { it != "" }
		.map { it.toInt()}
}