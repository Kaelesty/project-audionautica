package com.kaelesty.audionautica.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.data.repos.MusicRepo
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.usecases.UploadTrackUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTrackViewModel @Inject constructor(
	private val uploadTrackUseCase: UploadTrackUseCase
): ViewModel() {

	private val _musicFile = MutableLiveData<Uri>()
	val musicFile: LiveData<Uri> get() = _musicFile

	private val _posterFile = MutableLiveData<Uri>()
	val posterFile: LiveData<Uri> get() = _posterFile

	private val _error = MutableLiveData<String>()
	val error: LiveData<String> get() = _error

	fun musicFileBrowsed(uri: Uri) {
		_musicFile.postValue(uri)
	}

	fun posterFileBrowsed(uri: Uri) {
		_posterFile.postValue(uri)
	}

	fun addTrack(
		artist: String,
		title: String,
	) {

		if (musicFile.value == null) {
			_error.postValue("Choose music file")
		}

		musicFile.value?.let {
			val track = Track(
				artist = artist,
				duration = 0,
				id = -1,
				musicFile = it,
				posterFile = posterFile.value,
				title = title
			)

			viewModelScope.launch(Dispatchers.IO) {
				uploadTrackUseCase(track)
			}
		}
	}
}