package com.kaelesty.audionautica.data.localplaylists

import com.kaelesty.audionautica.data.mappers.PlaylistMapper
import com.kaelesty.audionautica.data.playlists.EntirePlaylistResponse
import com.kaelesty.audionautica.domain.localplaylists.LocalPlaylist
import javax.inject.Inject

class LocalPlaylistsMapper @Inject constructor(
	private val playlistsMapper: PlaylistMapper
) {

	fun mapLocalPlaylistDbModelToDomain(localPlaylistDbModel: LocalPlaylistDbModel) = LocalPlaylist(
		localPlaylistDbModel.id,
		localPlaylistDbModel.title,
		localPlaylistDbModel.desc,
		localPlaylistDbModel.isPullable,
		localPlaylistDbModel.remoteId,
		localPlaylistDbModel.trackIds
	)

	fun mapLocalPlaylistDomainToDbModel(localPlaylist: LocalPlaylist) = LocalPlaylistDbModel(
		localPlaylist.id,
		localPlaylist.title,
		localPlaylist.desc,
		localPlaylist.isPullable,
		localPlaylist.remoteId,
		localPlaylist.trackIds
	)

	fun mapEntirePlaylistResponseToLocalPlaylistDbModel(remoteId: Int, entirePlaylistResponse: EntirePlaylistResponse) = LocalPlaylistDbModel(
		id = 0,
		title = entirePlaylistResponse.title,
		desc = entirePlaylistResponse.description,
		isPullable = entirePlaylistResponse.isEditable,
		remoteId = remoteId,
		trackIds = playlistsMapper.trackIdsStringToList(entirePlaylistResponse.trackIds)
	)
}