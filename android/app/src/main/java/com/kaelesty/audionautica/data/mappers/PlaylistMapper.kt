package com.kaelesty.audionautica.data.mappers


import com.kaelesty.audionautica.data.local.dbmodels.PlaylistDbModel
import com.kaelesty.audionautica.data.playlists.EntirePlaylistResponse
import com.kaelesty.audionautica.data.playlists.UserPlaylistResponse
import com.kaelesty.audionautica.data.remote.api.ApiServiceFactory
import com.kaelesty.audionautica.data.repos.tools.UrlTool
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import javax.inject.Inject

class PlaylistMapper @Inject constructor() {

	val TRACK_IDS_STRINGIFICATION_DELIMITER = "/"

	fun DbModelToDomain(dbModel: PlaylistDbModel): UserPlaylist.Entire? {
		return UserPlaylist.Entire(
			id = dbModel.id,
			title = dbModel.title ?: return null,
			desc = dbModel.desc ?: return null,
			isEditable = dbModel.isEditable ?: return null,
			isPrivate = dbModel.isPrivate ?: return null,
			imageUrl = dbModel.imageUrl ?: return null,
			trackIds = dbModel.trackIds ?: return null,
			likes = dbModel.likes ?: return null,
		)
	}

	fun domainToDbModel(domain: UserPlaylist.Entire): PlaylistDbModel {
		return PlaylistDbModel(
			id = domain.id,
			likes = domain.likes,
			trackIds = domain.trackIds,
			desc = domain.desc,
			title = domain.title,
			imageUrl = domain.imageUrl,
			isPrivate = domain.isPrivate,
			isEditable = domain.isEditable
		)
	}

	fun domainToDbModel(domain: UserPlaylist.IdOnly): PlaylistDbModel {
		return PlaylistDbModel(
			id = domain.id,
			likes = null,
			trackIds = null,
			desc = null,
			title = null,
			imageUrl = null,
			isPrivate = null,
			isEditable = null
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
		trackIds = res.trackIds.split(TRACK_IDS_STRINGIFICATION_DELIMITER)
			.filter { it != "" }
			.map { it.toInt()},
		likes = res.likes,
		imageUrl = imageUrl
	)
}