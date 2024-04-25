package com.kaelesty.audionautica.presentation.playlists.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.kaelesty.audionautica.data.player.PlayerQueueController
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.domain.usecases.GetTrackByIdCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistDetailsViewModel @Inject constructor(
	private val getTrackByIdCase: GetTrackByIdCase,
	private val pqc: PlayerQueueController
): ViewModel() {

	private val _tracksFlow = MutableStateFlow<List<Track>>(listOf())
	val tracksFlow: StateFlow<List<Track>> get() = _tracksFlow

	val player: StateFlow<Player?> = pqc.getPlayerFlow()

	private var playlist_id = -1

	// Fix state unthrow bug!


	fun play() {
		if (_tracksFlow.value.isEmpty()) return
		viewModelScope.launch(Dispatchers.IO) {
			pqc.setQueue(_tracksFlow.value)
		}
	}
}