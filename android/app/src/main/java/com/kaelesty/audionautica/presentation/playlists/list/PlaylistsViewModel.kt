package com.kaelesty.audionautica.presentation.playlists.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.playlists.GetPlaylistCollectionUseCase
import com.kaelesty.audionautica.domain.playlists.NukePlaylistsCacheUseCase
import com.kaelesty.audionautica.domain.playlists.UpdatePlaylistCollectionUseCase
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistsViewModel @Inject constructor(
	private val getPlaylistCollectionUseCase: GetPlaylistCollectionUseCase,
	private val updatePlaylistCollectionUseCase: UpdatePlaylistCollectionUseCase,

	private val nukePlaylistsCacheUseCase: NukePlaylistsCacheUseCase
): ViewModel() {

	var playlistCollection: LiveData<List<UserPlaylist.Entire>> = getPlaylistCollectionUseCase()
		get() {
			if (field.value.isNullOrEmpty()) {
				updatePlaylistCollection()
			}
			return field
		}
	fun updatePlaylistCollection() {
		viewModelScope.launch(Dispatchers.IO) {
			nukePlaylistsCacheUseCase()
			delay(30)
			updatePlaylistCollectionUseCase()
		}
	}
}