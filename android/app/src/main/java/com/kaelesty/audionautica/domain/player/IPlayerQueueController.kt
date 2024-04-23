package com.kaelesty.audionautica.domain.player

import androidx.media3.common.Player
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IPlayerQueueController {

	suspend fun setQueue(tracks: List<Track>)

	suspend fun setQueueAsPlaylist(playlist: UserPlaylist.Entire, tracks: List<Track>)

	fun getQueueFlow(): SharedFlow<PlayerQueueCommand>

	suspend fun dropQueue()

	suspend fun setPlayer(player: Player)

	fun getPlayerFlow(): StateFlow<Player?>
}