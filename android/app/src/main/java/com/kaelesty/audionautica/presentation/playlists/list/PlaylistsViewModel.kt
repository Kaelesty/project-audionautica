package com.kaelesty.audionautica.presentation.playlists.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.playlists.GetPlaylistsUseCase
import com.kaelesty.audionautica.domain.playlists.LoadEntirePlaylistUseCase
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.domain.playlists.UserPlaylists
import com.kaelesty.audionautica.domain.usecases.GetTrackByIdCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistsViewModel @Inject constructor(
	private val getPlaylistsUseCase: GetPlaylistsUseCase,
	private val loadEntirePlaylistUseCase: LoadEntirePlaylistUseCase,
	private val getTrackByIdUseCase: GetTrackByIdCase,
): ViewModel() {

	private val _stateFlow: MutableStateFlow<PlaylistsState> = MutableStateFlow(PlaylistsState.Loading)
	val stateFlow: StateFlow<PlaylistsState> get() = _stateFlow.asStateFlow()

	fun updatePlaylist(id: Int) {
		viewModelScope.launch(Dispatchers.IO) {
			val loaded = loadEntirePlaylistUseCase(id)
			_stateFlow.emit(
				PlaylistsState.Result(
					playlists = (_stateFlow.value as PlaylistsState.Result)
						.playlists
						.map {
							if (it.id == id) {
								loaded
							} else it
						}
				)
			)
		}
	}

	fun loadPlaylists() {
		if (_stateFlow.value is PlaylistsState.Result) {
			return
		}
		viewModelScope.launch(Dispatchers.IO) {
			val res = getPlaylistsUseCase()
			val playlists = mutableListOf<UserPlaylist>()

			when (res) {
				is UserPlaylists.Result -> {
					(res.liked + res.created.filter { !res.liked.contains(it) }).forEach {
						playlists.add(
							UserPlaylist.IdOnly(it)
						)
					}
				}
				is UserPlaylists.Error -> {
					_stateFlow.emit(
						PlaylistsState.Loading
					)
					loadPlaylists()
					return@launch
				}
			}

			_stateFlow.emit(
				if (playlists.isEmpty()) {
					PlaylistsState.Empty
				}
				else {
					PlaylistsState.Result(
						playlists = playlists
					)
				}
			)
		}
	}
}