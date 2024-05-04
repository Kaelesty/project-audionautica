package com.kaelesty.audionautica.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.data.player.PlayerController
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.domain.usecases.GetAllTracksUseCase
import com.kaelesty.audionautica.domain.usecases.SaveTrackUseCase
import com.kaelesty.audionautica.domain.usecases.SearchTracksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
	private val getAllTracksUseCase: GetAllTracksUseCase,
	private val tracksSearchTracksUseCase: SearchTracksUseCase,
	private val pc: PlayerController,
	private val saveTrackUseCase: SaveTrackUseCase,
): ViewModel() {

	private val _stateFlow = MutableStateFlow<SearchState>(SearchState.Initial)
	val stateFlow: StateFlow<SearchState> get() = _stateFlow

	val tracksLibrary = getAllTracksUseCase()

	private fun searchTracks(query: String) {
		// if (query.length < 3) return
		viewModelScope.launch(Dispatchers.IO) {
			_stateFlow.emit(SearchState.Loading)
			_stateFlow.emit(
				SearchState.TracksResult(
					tracksSearchTracksUseCase(query)
				)
			)
		}
	}

	private fun searchPlaylists(query: String) {
		// if (query.length < 3) return
		viewModelScope.launch(Dispatchers.IO) {
			_stateFlow.emit(SearchState.Loading)


			// TODO("search playlists")


		}
	}

	fun search(query: String, searchType: SearchType) {
		when (searchType) {
			SearchType.Tracks -> {
				searchTracks(query)
			}
			SearchType.Playlists -> {
				searchPlaylists(query)
			}
		}
	}

	fun saveTrackToLibrary(track: Track) {
		viewModelScope.launch(Dispatchers.IO) {
			saveTrackUseCase(track)
		}
	}

	fun playTrack(track: Track) {
		viewModelScope.launch(Dispatchers.IO) {
			val playerState = pc.getStateFlow().value
			if (playerState.playing) {
				if (playerState.meta?.id == track.id) {
					pc.pause()
				}
				else {
					pc.play()
				}
			}
			else {
				pc.setQueue(listOf(track))
			}
		}
	}

}

enum class SearchType(type: String) {
	Tracks("Tracks"),
	Playlists("Playlists")
}

sealed class SearchState {

	object Initial: SearchState()

	object Loading: SearchState()

	data class TracksResult(
		val tracks: List<Track>
	): SearchState()

	data class PlaylistsResult(
		val playlists: List<UserPlaylist.Entire>
	): SearchState()
}