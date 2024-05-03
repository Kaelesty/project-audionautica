package com.kaelesty.audionautica.presentation.playlists.addtracktoplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.playlists.AddTrackToPlaylistUseCase
import com.kaelesty.audionautica.domain.playlists.GetPlaylistCollectionUseCase
import com.kaelesty.audionautica.domain.playlists.UpdatePlaylistCollectionUseCase
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.domain.returncodes.CheckConnectionRC
import com.kaelesty.audionautica.domain.usecases.CheckConnectionUseCase
import com.kaelesty.audionautica.presentation.toastmanager.ToastManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTrackToPlaylistViewModel @Inject constructor(
	private val getPlaylistCollectionUseCase: GetPlaylistCollectionUseCase,
	private val updatePlaylistCollectionUseCase: UpdatePlaylistCollectionUseCase,

	private val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase,

	private val toastManager: ToastManager,
	private val checkConnectionUseCase: CheckConnectionUseCase
): ViewModel() {

	val playlistCollection: LiveData<List<UserPlaylist.Entire>> = getPlaylistCollectionUseCase()
		get() {
			if (field.value.isNullOrEmpty()) {
				viewModelScope.launch(Dispatchers.IO) {
					updatePlaylistCollectionUseCase()
				}
			}
			return field
		}

	fun addTrackToPlaylist(trackId: Int, playlistId: Int) {
		viewModelScope.launch(Dispatchers.IO) {
			addTrackToPlaylistUseCase(trackId, playlistId)
			updatePlaylistCollectionUseCase()
		}
	}
}