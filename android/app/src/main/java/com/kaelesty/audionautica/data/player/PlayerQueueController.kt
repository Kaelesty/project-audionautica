package com.kaelesty.audionautica.data.player

import androidx.media3.common.Player
import com.kaelesty.audionautica.data.repos.MusicRepo
import com.kaelesty.audionautica.domain.entities.PlayReadyTrack
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.player.IPlayerQueueController
import com.kaelesty.audionautica.domain.player.PlayerQueueCommand
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PlayerQueueController @Inject constructor(
	private val musicRepo: MusicRepo
) : IPlayerQueueController {

	private val _queueCommands = MutableSharedFlow<PlayerQueueCommand>()
	val queueCommands: SharedFlow<PlayerQueueCommand> get() = _queueCommands

	private val _player = MutableStateFlow<Player?>(null)
	val player: StateFlow<Player?> get() = _player

	override suspend fun setQueue(tracks: List<Track>) {
		dropQueue()
		tracks.forEach { track ->
			_queueCommands.emit(
				PlayerQueueCommand.AddToQueue(
					PlayReadyTrack(
						metadata = track,
						uri = musicRepo.getTrackUri(track.id)
					)
				)
			)
		}
	}

	override fun getQueueFlow(): SharedFlow<PlayerQueueCommand> {
		return queueCommands
	}

	override suspend fun dropQueue() {
		_queueCommands.emit(PlayerQueueCommand.DropQueue)
	}

	override suspend fun setPlayer(player: Player) {
		_player.emit(player)
	}

	override fun getPlayerFlow(): StateFlow<Player?> {
		return player
	}
}