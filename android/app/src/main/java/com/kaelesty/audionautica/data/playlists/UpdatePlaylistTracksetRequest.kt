package com.kaelesty.audionautica.data.playlists

import com.google.gson.annotations.SerializedName

data class UpdatePlaylistTracksetRequest(
	@SerializedName("playlist_id") val playlistId: Int,
	@SerializedName("tracksid") val trackIds: List<Int>
)
