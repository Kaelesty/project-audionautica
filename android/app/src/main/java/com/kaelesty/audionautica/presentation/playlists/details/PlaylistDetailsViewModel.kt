package com.kaelesty.audionautica.presentation.playlists.details

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kaelesty.audionautica.data.player.PlayerController
import com.kaelesty.audionautica.domain.entities.Track
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
	private val pqc: PlayerController
) : ViewModel() {

	private val _stateFlow: MutableStateFlow<PlaylistDetailsState> =
		MutableStateFlow(PlaylistDetailsState.Empty)
	val stateFlow: StateFlow<PlaylistDetailsState> get() = _stateFlow

	private val scope = CoroutineScope(Dispatchers.IO)

	fun setPlaylist(playlist: UserPlaylist.Entire) {
		val currentPlaylist: UserPlaylist.Entire? = when (_stateFlow.value) {
			is PlaylistDetailsState.TracksLoaded -> {
				(_stateFlow.value as PlaylistDetailsState.TracksLoaded).playlist
			}

			is PlaylistDetailsState.LoadingTracks -> {
				(_stateFlow.value as PlaylistDetailsState.LoadingTracks).playlist
			}

			is PlaylistDetailsState.Empty -> null
		}

		fun loadTracks(playlist: UserPlaylist.Entire) {
			scope.launch(Dispatchers.IO) {
				_stateFlow.emit(
					PlaylistDetailsState.LoadingTracks(playlist)
				)
				val tracks = mutableListOf<Track>()
				playlist.trackIds.forEach {
					getTrackByIdCase(it)?.let { track ->
						tracks.add(track)
					}
				}
				_stateFlow.emit(
					PlaylistDetailsState.TracksLoaded(
						playlist, tracks
					)
				)
			}
		}

		if (currentPlaylist == null) {
			loadTracks(playlist)
		} else if (currentPlaylist.id != playlist.id) {
			loadTracks(playlist)
		}

	}

	private suspend fun sendPlaylistToPlay() {
		val playerState = pqc.getStateFlow().value
		val detailsState = _stateFlow.value
		if (detailsState is PlaylistDetailsState.TracksLoaded) {
			val playlist = detailsState.playlist
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
					if (playerState.playlistId != detailsState.playlist.id) {
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

		class LoadingTracks(val playlist: UserPlaylist.Entire) : PlaylistDetailsState()

		class TracksLoaded(val playlist: UserPlaylist.Entire, val tracks: List<Track>) :
			PlaylistDetailsState()
	}
}