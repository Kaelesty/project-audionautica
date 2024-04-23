package com.kaelesty.audionautica.data.mappers


import com.kaelesty.audionautica.data.playlists.EntirePlaylistResponse
import com.kaelesty.audionautica.data.playlists.UserPlaylistResponse
import com.kaelesty.audionautica.data.remote.api.ApiServiceFactory
import com.kaelesty.audionautica.data.repos.tools.UrlTool
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import javax.inject.Inject

class PlaylistMapper @Inject constructor() {

	val TRACK_IDS_STRINGIFICATION_DELIMITER = "/"

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