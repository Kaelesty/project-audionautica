package com.kaelesty.audionautica.presentation.playlists

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.playlists.Playlist
import com.kaelesty.audionautica.domain.player.IPlayerQueueController
import com.kaelesty.audionautica.domain.playlists.GetPlaylistsUseCase
import com.kaelesty.audionautica.domain.playlists.UploadPlaylistUseCase
import com.kaelesty.audionautica.domain.playlists.UserPlaylists
import com.kaelesty.audionautica.domain.usecases.GetAllPlaylistsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistsViewModel @Inject constructor(
	private val pqc: IPlayerQueueController,
	private val getPlaylistsUseCase: GetPlaylistsUseCase,
	private val uploadPlaylistUseCase: UploadPlaylistUseCase,
): ViewModel() {

	private val _stateFlow: MutableStateFlow<PlaylistsState> = MutableStateFlow(PlaylistsState.Loading)
	val stateFlow: StateFlow<PlaylistsState> get() = _stateFlow.asStateFlow()

	fun loadPlaylists() {
		viewModelScope.launch(Dispatchers.IO) {
			val res = getPlaylistsUseCase()
			_stateFlow.emit(
				when (res) {
					is UserPlaylists.Loading -> {
						PlaylistsState.Loading
					}

					is UserPlaylists.Empty -> {
						PlaylistsState.Empty
					}

					is UserPlaylists.Result -> {
						PlaylistsState.Result(
							cards = res.playlists_ids.map {
								PlaylistCardState.IdOnly(it)
							}
						)
					}
				}
			)
		}
	}
}