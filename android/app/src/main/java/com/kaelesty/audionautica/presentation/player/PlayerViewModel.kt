package com.kaelesty.audionautica.presentation.player

import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.player.IPlayerController
import com.kaelesty.audionautica.domain.player.PlayerState
import com.kaelesty.audionautica.presentation.getCurrentPlaylistId
import com.kaelesty.audionautica.presentation.getCurrentTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
	val pc: IPlayerController
): ViewModel() {

	val playerState: StateFlow<PlayerState> = pc.getStateFlow()
	val commandFlow = pc.getCommandFlow()

	private var player: Player? = null

	fun setPlayer(player: Player) {
		viewModelScope.launch(Dispatchers.IO) {
			pc.setPlayer(player)
		}
	}
}