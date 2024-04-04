package com.kaelesty.audionautica.presentation.playlists

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.playlists.Playlist
import com.kaelesty.audionautica.domain.player.IPlayerQueueController
import com.kaelesty.audionautica.domain.playlists.UploadPlaylistUseCase
import com.kaelesty.audionautica.domain.usecases.GetAllPlaylistsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistsViewModel @Inject constructor(
	private val pqc: IPlayerQueueController,
	private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
	private val uploadPlaylistUseCase: UploadPlaylistUseCase,
): ViewModel() {

	private val _playlistsFlow: MutableStateFlow<List<Playlist>> = MutableStateFlow(listOf())
	val playlistsFlow: StateFlow<List<Playlist>> get() = _playlistsFlow.asStateFlow()

	private val _errorFlow: MutableStateFlow<String> = MutableStateFlow("")
	val errorFlow: StateFlow<String> get() = _errorFlow

	fun loadPlaylists() {
		viewModelScope.launch(Dispatchers.IO) {
//			_playlistsFlow.emit(
//				getAllPlaylistsUseCase()
//			)
		}
	}

	fun uploadPlaylist(
		uri: Uri,
		title: String,
		desc: String,
		isPrivate: Boolean,
		trackIds: List<Int>,
	) {
		if (title.length < 3 || title.length > 15) {
			viewModelScope.launch(Dispatchers.IO) {
				_errorFlow.emit("Title must consist of 3 to 15 characters")
			}
			return
		}
		if (desc.length < 3 || desc.length > 40) {
			viewModelScope.launch(Dispatchers.IO) {
				_errorFlow.emit("Description must consist of 3 to 40 characters")
			}
			return
		}
		viewModelScope.launch(Dispatchers.IO) {
			uploadPlaylistUseCase(
				Playlist(
					id = -1,
					creatorId = -1,
					trackIds = trackIds,
					title = title,
					isPrivate = isPrivate,
					desc = desc,
					imageUrl = ""
				),
				uri
			)
		}
	}
}