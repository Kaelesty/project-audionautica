package com.kaelesty.audionautica.domain.player

import com.kaelesty.audionautica.domain.entities.PlayReadyTrack
import com.kaelesty.audionautica.domain.playlists.UserPlaylist

sealed class PlayerQueueCommand {

	object DropQueue: PlayerQueueCommand()

	class AddToQueue(val track: PlayReadyTrack): PlayerQueueCommand()

	class SetPlaylist(val playlist: UserPlaylist.Entire): PlayerQueueCommand()
}