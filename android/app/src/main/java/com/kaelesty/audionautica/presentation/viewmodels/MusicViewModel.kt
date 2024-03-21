package com.kaelesty.audionautica.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.player.IPlayerQueueController
import com.kaelesty.audionautica.domain.usecases.AddToTracksQueueUseCase
import com.kaelesty.audionautica.domain.usecases.AddTrackToPlaylistUseCase
import com.kaelesty.audionautica.domain.usecases.CreatePlaylistUseCase
import com.kaelesty.audionautica.domain.usecases.DeletePlaylistUseCase
import com.kaelesty.audionautica.domain.usecases.DeleteTrackFromPlaylistUseCase
import com.kaelesty.audionautica.domain.usecases.DeleteTrackUseCase
import com.kaelesty.audionautica.domain.usecases.GetAllPlaylistsUseCase
import com.kaelesty.audionautica.domain.usecases.GetAllTracksUseCase
import com.kaelesty.audionautica.domain.usecases.GetPlaylistTracksUseCase
import com.kaelesty.audionautica.domain.usecases.PlayNextUseCase
import com.kaelesty.audionautica.domain.usecases.PlayPrevUseCase
import com.kaelesty.audionautica.domain.usecases.SaveTrackUseCase
import com.kaelesty.audionautica.domain.usecases.SearchTracksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicViewModel @Inject constructor(
	private val searchTracksUseCase: SearchTracksUseCase,
	private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
	private val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase,
	private val deleteTrackFromPlaylistUseCase: DeleteTrackFromPlaylistUseCase,
	private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase,
	private val createPlaylistUseCase: CreatePlaylistUseCase,
	private val getAllTracksUseCase: GetAllTracksUseCase,
	private val deletePlaylistUseCase: DeletePlaylistUseCase,
	private val saveTrackUseCase: SaveTrackUseCase,
	private val deleteTrackUseCase: DeleteTrackUseCase,
	private val pqc: IPlayerQueueController
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
			pqc.setQueue(
				listOf(track)
			)
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

	fun getPlaylists(): LiveData<List<Playlist>> {
		return getAllPlaylistsUseCase()
	}

	fun removeTrackFromPlaylist(track: Track, id: Int) {
		viewModelScope.launch(Dispatchers.IO) {
			deleteTrackFromPlaylistUseCase(track, id)
		}
	}

	fun getPlaylistTracks(id: Int): List<Track> {
		return getPlaylistTracksUseCase(id)
	}

	fun createPlaylist(playlist: Playlist) {
		viewModelScope.launch(Dispatchers.IO) {
			createPlaylistUseCase(playlist)
		}
	}

	fun deletePlaylist(playlistId: Int) {
		viewModelScope.launch(Dispatchers.IO) {
			deletePlaylistUseCase(playlistId)
		}
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