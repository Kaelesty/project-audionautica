package com.kaelesty.audionautica.domain.player

import com.kaelesty.audionautica.domain.entities.PlayReadyTrack
import com.kaelesty.audionautica.domain.playlists.UserPlaylist

sealed class PlayerQueueCommand {

	object Drop: PlayerQueueCommand()

	class Play(val track: PlayReadyTrack): PlayerQueueCommand()
}