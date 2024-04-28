package com.kaelesty.audionautica.data.player

import androidx.media3.common.Player
import com.kaelesty.audionautica.data.repos.MusicRepo
import com.kaelesty.audionautica.domain.entities.PlayReadyTrack
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.player.IPlayerController
import com.kaelesty.audionautica.domain.player.PlayerQueueCommand
import com.kaelesty.audionautica.domain.player.PlayerState
import com.kaelesty.audionautica.domain.player.Repeat
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerController (
	private val musicRepo: MusicRepo
) : IPlayerController {

	private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
	private val positionUpdateScope = CoroutineScope(Dispatchers.Main)

	private val _queueCommands = MutableSharedFlow<PlayerQueueCommand>()
	val queueCommands: SharedFlow<PlayerQueueCommand> get() = _queueCommands

	private val _stateFlow = MutableStateFlow(PlayerState(null, false, 0, 0, 0, false, Repeat.False))

	private val _skippingFlow = MutableStateFlow(false)

	private var player: Player? = null

	private var shuffle: Boolean = false
	private var repeat: Repeat = Repeat.False

	private var playlist: UserPlaylist.Entire? = null
	private var tracks: List<Track> = listOf()
	private var originalTracks: List<Track> = listOf()
	private var currentPosition: Int = 0

	init {
		positionUpdateScope.launch {
			while (true) {
				_stateFlow.emit(
					_stateFlow.value.copy(
						progress = player?.currentPosition ?: 0
					)
				)
				delay(500)
			}
		}
	}

	private suspend fun submitNextTrack(stayAtPosition: Boolean = false, force: Boolean = false) {
		dropQueue()
		if (!stayAtPosition) {
			if (repeat == Repeat.One && !force) {

			}
			else {
				currentPosition += 1
				if (currentPosition == tracks.size) {
					if (repeat == Repeat.True || repeat == Repeat.One) {
						currentPosition = 0
					}
					else {
						currentPosition -= 1
						_playFromPosition(currentPosition)
						player?.pause()
						return
					}

				}
			}

		}
		_stateFlow.emit(
			_stateFlow.value.copy(
				currentQueuePosition = currentPosition
			)
		)
		_playFromPosition(currentPosition)
	}

	private suspend fun submitPrevTrack() {

		if (currentPosition != 0) {
			currentPosition -= 1
		}
		else {
			if (repeat == Repeat.True) {
				currentPosition = tracks.size - 1
			}
			else if (repeat == Repeat.False) {
				submitNextTrack(stayAtPosition = true)
				player?.pause()
			}
			else {

			}
		}

		dropQueue()
		_stateFlow.emit(
			_stateFlow.value.copy(
				currentQueuePosition = currentPosition
			)
		)
		_playFromPosition(currentPosition)
	}

	override fun setPlaybackSpeed(speed: Float) {
		player?.setPlaybackSpeed(speed)
	}

	override fun playFromPosition(pos: Int) {
		scope.launch(Dispatchers.Main) {
			_skippingFlow.emit(true)
			dropQueue()
			_playFromPosition(pos)
			_stateFlow.emit(
				_stateFlow.value.copy(
					currentQueuePosition = pos
				)
			)
			currentPosition = pos
			_skippingFlow.emit(false)
		}
	}

	private suspend fun _playFromPosition(position: Int) {
		val track = tracks[position]
		_queueCommands.emit(
			PlayerQueueCommand.Play(
				PlayReadyTrack(
					metadata = track,
					uri = musicRepo.getTrackUri(track.id),
				)
			)
		)
		_stateFlow.emit(
			_stateFlow.value.copy(
				meta = track,
			)
		)
	}

	override suspend fun setQueue(tracks_: List<Track>, startPosition: Int) {
		tracks = tracks_
		currentPosition = startPosition
		submitNextTrack(true)
		scope.launch {
			_stateFlow.emit(
				_stateFlow.value.copy(
					queue = tracks_
				)
			)
		}
	}

	override suspend fun setQueueAsPlaylist(
		playlist_: UserPlaylist.Entire,
		tracks_: List<Track>,
		startPosition: Int
	) {
		playlist = playlist_
		_stateFlow.emit(
			_stateFlow.value.copy(
				playlistId = playlist_.id
			)
		)
		setQueue(tracks_, startPosition)
	}

	override suspend fun dropQueue() {
		_queueCommands.emit(PlayerQueueCommand.Drop)
	}

	override suspend fun setPlayer(player_: Player) {
		player = player_
		player_.addListener(
			object : Player.Listener {
				override fun onPlaybackStateChanged(playbackState: Int) {
					super.onPlaybackStateChanged(playbackState)

					if (playbackState == Player.STATE_ENDED && !_skippingFlow.value) {
						scope.launch(Dispatchers.Main) {
							submitNextTrack()
						}
					}
					else if (playbackState == Player.STATE_READY) {
						scope.launch(Dispatchers.Main) {
							_stateFlow.emit(
								_stateFlow.value.copy(
									duration = player?.duration ?: 0
								)
							)
						}
					}
				}


				override fun  onIsPlayingChanged(isPlaying: Boolean) {
					super.onIsPlayingChanged(isPlaying)
					scope.launch(Dispatchers.IO) {
						_stateFlow.emit(
							_stateFlow.value.copy(
								playing = isPlaying
							)
						)
					}
				}
			}
		)
	}

	override fun getCommandFlow(): SharedFlow<PlayerQueueCommand> {
		return queueCommands
	}

	override fun playNext() {
		scope.launch(Dispatchers.Main) {
			_skippingFlow.emit(true)
			submitNextTrack(force = true)
			_skippingFlow.emit(false)
		}
	}

	override fun playPrev() {
		scope.launch(Dispatchers.Main) {
			_skippingFlow.emit(true)
			submitPrevTrack()
			_skippingFlow.emit(false)
		}
	}

	override fun setShuffle(state: Boolean) {
		shuffle = state
		tracks = if (shuffle) {
			originalTracks.shuffled()
		} else {
			originalTracks
		}
		scope.launch {
			_stateFlow.emit(
				_stateFlow.value.copy(
					shuffle = shuffle
				)
			)
		}
	}

	override fun setRepeat(state: Repeat) {
		repeat = state
		scope.launch {
			_stateFlow.emit(
				_stateFlow.value.copy(
					repeat = state
				)
			)
		}
	}

	override fun play() {
		player?.play()
	}

	override fun pause() {
		player?.pause()
	}

	override fun getStateFlow(): StateFlow<PlayerState> {
		return _stateFlow.asStateFlow()
	}

	override fun seekTo(progress: Long) {
		player?.seekTo(progress)
	}
}