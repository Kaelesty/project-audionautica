package com.kaelesty.audionautica.presentation.playlists.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.playlists.UploadPlaylistUseCase
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistUploadViewModel @Inject constructor(
	private val uploadPlaylistUseCase: UploadPlaylistUseCase,
): ViewModel() {

	private val _stateFlow: MutableStateFlow<PlaylistUploadState> = MutableStateFlow(PlaylistUploadState.Initial)
	val stateFlow: StateFlow<PlaylistUploadState> get() = _stateFlow

	fun resetState() {
		viewModelScope.launch(Dispatchers.IO) {
			_stateFlow.emit(PlaylistUploadState.Initial)
		}
	}

	fun uploadPlaylist(
		uri: Uri?,
		title: String,
		desc: String,
		isPrivate: Boolean,
		trackIds: List<Int>,
	) {
		if (title.length < 3 || title.length > 15) {
			viewModelScope.launch(Dispatchers.IO) {
				_stateFlow.emit(
					PlaylistUploadState.Error("Title must consist of 3 to 15 characters")
				)
			}
			return
		}
		if (desc.length < 3 || desc.length > 40) {
			viewModelScope.launch(Dispatchers.IO) {
				_stateFlow.emit(
					PlaylistUploadState.Error("Description must consist of 3 to 40 characters")
				)
			}
			return
		}
		if (uri == null || uri == Uri.EMPTY) {
			viewModelScope.launch(Dispatchers.IO) {
				_stateFlow.emit(
					PlaylistUploadState.Error("Pick image")
				)
			}
			return
		}
		viewModelScope.launch(Dispatchers.IO) {
			_stateFlow.emit(PlaylistUploadState.Uploading)
			val result = uploadPlaylistUseCase(
				UserPlaylist.Entire(
					id = -1,
					isEditable = true,
					trackIds = trackIds,
					title = title,
					isPrivate = isPrivate,
					desc = desc,
					imageUrl = "",
					likes = 0
				),
				uri
			)

			if (result) {
				_stateFlow.emit(PlaylistUploadState.Finished)
			}
			else {
				_stateFlow.emit(PlaylistUploadState.Error("Loading Error"))
			}
		}
	}
}