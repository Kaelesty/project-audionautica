package com.kaelesty.audionautica.presentation.player

import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.player.IPlayerQueueController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
	private val pqc: IPlayerQueueController
): ViewModel() {

	data class PlayerState(
		val meta: Track?,
		val playing: Boolean,
		val progress: Long,
		val duration: Long,
	)

	private val _playerState = MutableStateFlow(
		PlayerState(
			null,
			false,
			0,
			0,
		)
	)
	val playerState: StateFlow<PlayerState> get() = _playerState

	private var player: Player? = null
	private var updateProgress = true
	private var seekingProgress: Float = 0f

	init {

		viewModelScope.launch(Dispatchers.Main) {
			while (true) {
				if (updateProgress) {
					player?.let {
						val dur = it.duration
						//Log.d("PlayerTag", "new player position: ${it.currentPosition}")
						_playerState.emit(
							_playerState.value.copy(
								progress = it.currentPosition,
								duration = if (dur > 0) dur else 0
							)
						)
					}
					delay(100)
				}

			}
		}

		viewModelScope.launch(Dispatchers.IO) {
			pqc.getPlayerFlow().collect {
				player = it
				Log.d("PlayerTag", if (it == null) "player not connected" else "player connected")
				it?.addListener(
					object : Player.Listener {

						override fun onMediaMetadataChanged(mediaMetadata: androidx.media3.common.MediaMetadata) {
							super.onMediaMetadataChanged(mediaMetadata)
							viewModelScope.launch(Dispatchers.Main) {
								_playerState.emit(
									_playerState.value.copy(
										meta = mediaMetadata.getCurrentTrack(),
									)
								)
							}
						}

						override fun onIsPlayingChanged(isPlaying: Boolean) {
							super.onIsPlayingChanged(isPlaying)
							viewModelScope.launch(Dispatchers.IO) {
								_playerState.emit(
									_playerState.value.copy(playing = isPlaying,)
								)
							}
						}
					}
				)
			}
		}
	}

	fun setPlayingProgress(progress: Float) {
		player?.let {
			it.seekTo(progress.toLong())
		}
	}

	fun setSeekingProgress(progress: Float) {
		updateProgress = false
		seekingProgress = progress
	}

	fun confirmSeek() {
		setPlayingProgress(seekingProgress)
		seekingProgress = 0f
		updateProgress = true
	}

	fun getQueueFlow() = pqc.getQueueFlow()

	fun setMediaController(mc: MediaController) {
		mc.registerCallback(
			object : MediaController.Callback() {

				override fun onMetadataChanged(metadata: MediaMetadata?) {
					super.onMetadataChanged(metadata)
					viewModelScope.launch(Dispatchers.IO) {
						val track = metadata?.getCurrentTrack()
						_playerState.emit(
							_playerState.value.copy(meta = track)
						)
					}
				}

				override fun onPlaybackStateChanged(state: PlaybackState?) {
					super.onPlaybackStateChanged(state)
					viewModelScope.launch(Dispatchers.IO) {
						_playerState.emit(
							_playerState.value.copy(playing = state?.state == PlaybackState.STATE_PLAYING)
						)
					}
				}
			}
		)
	}

	fun setPlayer(player: Player) {
		viewModelScope.launch(Dispatchers.IO) {
			pqc.setPlayer(player)
		}
	}
}