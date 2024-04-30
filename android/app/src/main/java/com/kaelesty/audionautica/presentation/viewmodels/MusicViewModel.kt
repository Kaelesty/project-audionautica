package com.kaelesty.audionautica.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.player.IPlayerController
import com.kaelesty.audionautica.domain.usecases.AddTrackToPlaylistUseCase
import com.kaelesty.audionautica.domain.usecases.DeleteTrackFromPlaylistUseCase
import com.kaelesty.audionautica.domain.usecases.DeleteTrackUseCase
import com.kaelesty.audionautica.domain.usecases.GetAllTracksUseCase
import com.kaelesty.audionautica.domain.usecases.GetPlaylistTracksUseCase
import com.kaelesty.audionautica.domain.usecases.SaveTrackUseCase
import com.kaelesty.audionautica.domain.usecases.SearchTracksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicViewModel @Inject constructor(
	private val searchTracksUseCase: SearchTracksUseCase,
	private val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase,
	private val deleteTrackFromPlaylistUseCase: DeleteTrackFromPlaylistUseCase,
	private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase,
	private val getAllTracksUseCase: GetAllTracksUseCase,
	private val saveTrackUseCase: SaveTrackUseCase,
	private val deleteTrackUseCase: DeleteTrackUseCase,
	private val pqc: IPlayerController
): ViewModel() {

	private val _tracksSearchResults = MutableLiveData<List<Track>>()
	val tracksSearchResults: LiveData<List<Track>> get() = _tracksSearchResults

	fun search(query: String) {
		viewModelScope.launch {
			val searchResults = searchTracksUseCase(query)
			_tracksSearchResults.postValue(searchResults)
		}
	}

	fun playTrack(track: Track) {
		viewModelScope.launch {
			val playerState = pqc.getStateFlow().value
			val pos = playerState.currentQueuePosition
			if (pos != -1 && playerState.queue?.get(pos)?.id == track.id) {
				if (playerState.playing) {
					pqc.pause()
				}
				else {
					pqc.play()
				}
			}
			else {
				pqc.setQueue(
					listOf(track)
				)
			}
		}
	}

	fun playPlaylist(id: Int) {
		viewModelScope.launch {
			val tracks = getPlaylistTracks(id)
			pqc.setQueue(tracks)
		}
	}

	fun addTrackToPlaylist(track: Track, playlistId: Int) {
		viewModelScope.launch(Dispatchers.IO) {
			addTrackToPlaylistUseCase(track, playlistId)
		}
	}

	fun removeTrackFromPlaylist(track: Track, id: Int) {
		viewModelScope.launch(Dispatchers.IO) {
			deleteTrackFromPlaylistUseCase(track, id)
		}
	}

	fun getPlaylistTracks(id: Int): List<Track> {
		return getPlaylistTracksUseCase(id)
	}


	fun getAllTracks(): LiveData<List<Track>> {
		return getAllTracksUseCase()
	}

	fun saveTrack(track: Track) {
		viewModelScope.launch(Dispatchers.IO) {
			saveTrackUseCase(track)
		}
	}

	fun deleteTrack(track: Track) {
		viewModelScope.launch(Dispatchers.IO) {
			deleteTrackUseCase(track)
		}
	}
}