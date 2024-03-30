package com.kaelesty.audionautica.presentation.player

import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.player.IPlayerQueueController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
	private val pqc: IPlayerQueueController
): ViewModel() {

	data class PlayerState(
		val meta: Track?,
		val playing: Boolean
	)

	private val _playerState = MutableStateFlow(
		PlayerState(
			null,
			false
		)
	)
	val playerState: StateFlow<PlayerState> get() = _playerState

	init {
		viewModelScope.launch(Dispatchers.IO) {
			pqc.getPlayerFlow().collect {
				Log.d("PlayerTag", if (it == null) "player not connected" else "player connected")
				it?.addListener(
					object : Player.Listener {

						override fun onMediaMetadataChanged(mediaMetadata: androidx.media3.common.MediaMetadata) {
							super.onMediaMetadataChanged(mediaMetadata)
							viewModelScope.launch(Dispatchers.IO) {
								_playerState.emit(
									PlayerState(
										meta = mediaMetadata.getCurrentTrack(),
										playing = _playerState.value.playing
									)
								)
							}
						}

						override fun onIsPlayingChanged(isPlaying: Boolean) {
							super.onIsPlayingChanged(isPlaying)
							viewModelScope.launch(Dispatchers.IO) {
								_playerState.emit(
									PlayerState(
										meta = _playerState.value.meta,
										playing = isPlaying
									)
								)
							}
						}
					}
				)

			}
		}
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
							PlayerState(
								track,
								_playerState.value.playing
							)
						)
					}
				}

				override fun onPlaybackStateChanged(state: PlaybackState?) {
					super.onPlaybackStateChanged(state)
					viewModelScope.launch(Dispatchers.IO) {
						_playerState.emit(
							PlayerState(
								_playerState.value.meta,
								state?.state == PlaybackState.STATE_PLAYING
							)
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