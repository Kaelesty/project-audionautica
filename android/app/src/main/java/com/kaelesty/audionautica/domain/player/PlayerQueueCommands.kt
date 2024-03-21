package com.kaelesty.audionautica.domain.player

import com.kaelesty.audionautica.domain.entities.PlayReadyTrack

sealed class PlayerQueueCommand {

	object DropQueue: PlayerQueueCommand()

	class AddToQueue(val track: PlayReadyTrack): PlayerQueueCommand()
}