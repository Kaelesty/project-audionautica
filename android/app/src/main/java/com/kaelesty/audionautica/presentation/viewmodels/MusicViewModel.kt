package com.kaelesty.audionautica.presentation.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.usecases.AddToTracksQueueUseCase
import com.kaelesty.audionautica.domain.usecases.AddTrackToPlaylistUseCase
import com.kaelesty.audionautica.domain.usecases.GetAllPlaylistsUseCase
import com.kaelesty.audionautica.domain.usecases.SearchTracksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicViewModel @Inject constructor(
	private val searchTracksUseCase: SearchTracksUseCase,
	private val addToTracksQueueUseCase: AddToTracksQueueUseCase,
	private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
	private val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase
): ViewModel() {

	private val _tracksSearchResults = MutableLiveData<List<Track>>()
	val tracksSearchResults: LiveData<List<Track>> get() = _tracksSearchResults

	private val _playedTrackUri = MutableLiveData<Uri>()
	val playedTrackUri: LiveData<Uri> get() = _playedTrackUri



	fun search(query: String) {
		viewModelScope.launch {
			val searchResults = searchTracksUseCase(query)
			_tracksSearchResults.postValue(searchResults)
		}
	}

	fun playTrack(track: Track) {
		viewModelScope.launch {
			addToTracksQueueUseCase(
				tracks = listOf(track),
				dropQueue = true,
			)
		}
	}

	fun addTrackToPlaylist(track: Track, playlistId: Int) {
		viewModelScope.launch(Dispatchers.IO) {
			addTrackToPlaylistUseCase(track, playlistId)
		}
	}

	fun getPlaylists(): LiveData<List<Playlist>> {
		return getAllPlaylistsUseCase()
	}

	fun pause() {

	}
}