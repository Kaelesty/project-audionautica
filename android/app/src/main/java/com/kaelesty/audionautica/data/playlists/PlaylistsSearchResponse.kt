package com.kaelesty.audionautica.data.playlists

import com.google.gson.annotations.SerializedName

data class PlaylistsSearchResponse(
	@SerializedName("playlists") val playlists: List<IdEntirePlaylistResponse>,
)


