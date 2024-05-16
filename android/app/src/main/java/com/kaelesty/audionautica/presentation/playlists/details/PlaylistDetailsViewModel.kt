package com.kaelesty.audionautica.presentation.playlists.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.data.player.PlayerController
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.playlists.DeletePlaylistUseCase
import com.kaelesty.audionautica.domain.playlists.DislikePlaylistUseCase
import com.kaelesty.audionautica.domain.playlists.LikePlaylistUseCase
import com.kaelesty.audionautica.domain.playlists.LoadCachedPlaylistUseCase
import com.kaelesty.audionautica.domain.playlists.LoadLiveEntirePlaylistUseCase
import com.kaelesty.audionautica.domain.playlists.UpdatePlaylistTracksetUseCase
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.domain.usecases.GetTrackByIdCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistDetailsViewModel @Inject constructor(
	private val getTrackByIdCase: GetTrackByIdCase,
	private val getCachedPlaylistUseCase: LoadCachedPlaylistUseCase,
	private val pqc: PlayerController,
	private val updatePlaylistTracksetUseCase: UpdatePlaylistTracksetUseCase,
	private val likePlaylistUseCase: LikePlaylistUseCase,
	private val dislikePlaylistUseCase: DislikePlaylistUseCase,
	private val deletePlaylistUseCase: DeletePlaylistUseCase,
) : ViewModel() {

	private val _stateFlow: MutableStateFlow<PlaylistDetailsState> =
		MutableStateFlow(PlaylistDetailsState.Empty)
	val stateFlow: StateFlow<PlaylistDetailsState> get() = _stateFlow

	private val scope = CoroutineScope(Dispatchers.IO)

	private var _playlist: MutableStateFlow<UserPlaylist.Entire?> = MutableStateFlow(null)
	val playlist: StateFlow<UserPlaylist.Entire?> get() = _playlist

	fun setPlaylist(playlistId: Int) {
		scope.launch(Dispatchers.IO) {
			val cached = getCachedPlaylistUseCase(playlistId, update = true)
			_playlist.emit(cached)
			loadTracks(cached as UserPlaylist.Entire)
		}
	}

	fun removePlaylistFromCollection(playlistId: Int) {
		scope.launch(Dispatchers.IO) {
			if (dislikePlaylistUseCase(playlistId)) {
				_playlist.emit(
					getCachedPlaylistUseCase(playlistId, update = true)
				)
			}
		}
	}

	fun addPlaylistToCollection(playlistId: Int) {
		scope.launch(Dispatchers.IO) {
			if (likePlaylistUseCase(playlistId)) {
				_playlist.emit(
					getCachedPlaylistUseCase(playlistId, update = true)
				)
			}
		}
	}

	fun deletePlaylist(playlistId: Int) {
		scope.launch(Dispatchers.IO) {
			deletePlaylistUseCase(playlistId)
		}
	}

	fun updatePlaylistTrackset(trackIds: List<Int>) {
		scope.launch(Dispatchers.IO) {
			if (updatePlaylistTracksetUseCase(_playlist.value?.id ?: return@launch, trackIds)) {
				loadTracks(_playlist.value as UserPlaylist.Entire)
			}
		}
	}

	fun loadTracks(playlist: UserPlaylist.Entire) {
		scope.launch(Dispatchers.IO) {
			_stateFlow.emit(
				PlaylistDetailsState.LoadingTracks()
			)
			val tracks = mutableListOf<Track>()
			playlist.trackIds.forEach {
				getTrackByIdCase(it)?.let { track ->
					tracks.add(track)
				}
			}
			_stateFlow.emit(
				PlaylistDetailsState.TracksLoaded(
					tracks
				)
			)
		}
	}

	private suspend fun sendPlaylistToPlay() {
		val playerState = pqc.getStateFlow().value
		val detailsState = _stateFlow.value
		if (detailsState is PlaylistDetailsState.TracksLoaded) {
			val playlist = (_playlist.value ?: return) as UserPlaylist.Entire
			if (playerState.playlistId == playlist.id) {
				if (playerState.playing) {
					pqc.pause()
				} else {
					pqc.play()
				}
			} else {
				pqc.setQueueAsPlaylist(
					playlist_ = playlist,
					tracks_ = detailsState.tracks
				)
			}
		}
	}

	fun play() {
		scope.launch(Dispatchers.Main) {
			sendPlaylistToPlay()
		}
	}

	fun playFromPosition(pos: Int) {
		try {
			scope.launch(Dispatchers.IO) {
				val playerState = pqc.getStateFlow().value
				val detailsState = _stateFlow.value

				if (detailsState is PlaylistDetailsState.TracksLoaded) {
					if (playerState.playlistId != _playlist.value?.id) {
						sendPlaylistToPlay()
						pqc.playFromPosition(pos)
					}
					else {
						if (playerState.currentQueuePosition == pos) {
							if (playerState.playing) {
								pqc.pause()
							}
							else {
								pqc.play()
							}
						}
						else {
							pqc.playFromPosition(pos)
						}
					}
				}
			}
		}
		catch (e: Exception){

		}
	}

	sealed class PlaylistDetailsState {

		object Empty : PlaylistDetailsState()

		class LoadingTracks : PlaylistDetailsState()

		class TracksLoaded(val tracks: List<Track>) :
			PlaylistDetailsState()
	}
}