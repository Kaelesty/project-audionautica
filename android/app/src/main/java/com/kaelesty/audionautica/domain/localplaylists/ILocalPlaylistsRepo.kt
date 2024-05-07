package com.kaelesty.audionautica.domain.localplaylists

import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.playlists.UserPlaylist

interface ILocalPlaylistsRepo {

	fun getLocalPlaylists(): LiveData<List<LocalPlaylist>>

	suspend fun saveLocalPlaylist(playlist: LocalPlaylist)

	suspend fun deleteLocalPlaylist(playlistId: Int)

	suspend fun pullLocalPlaylist(playlist: LocalPlaylist)

	suspend fun pushLocalPlaylist(playlist: LocalPlaylist)
}