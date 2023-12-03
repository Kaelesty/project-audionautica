package com.kaelesty.audionautica.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackExp
import com.kaelesty.audionautica.domain.returncodes.UploadTrackRC
import com.kaelesty.audionautica.domain.usecases.UploadTrackUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTrackViewModel @Inject constructor(
	private val uploadTrackUseCase: UploadTrackUseCase
): ViewModel() {

	private val _errorFlow = MutableSharedFlow<String>()
	val errorFlow: SharedFlow<String> get() = _errorFlow

	private val _loadingFlow = MutableSharedFlow<Boolean>()
	val loadingFlow: SharedFlow<Boolean> get() = _loadingFlow

	private val _finish = MutableLiveData<Unit>()
	val finish: LiveData<Unit> get() = _finish

	fun addTrack(
		artist: String,
		title: String,
		uri: Uri,
		tags: List<String>,
	) {
		viewModelScope.launch {
			_loadingFlow.emit(true)
		}
		if (uri == Uri.EMPTY) {
			viewModelScope.launch(Dispatchers.IO) {
				_errorFlow.emit("Choose music file")
				_loadingFlow.emit(false)
			}
			return
		}

		val track = Track(
			id = -1,
			title = title,
			artist = artist,
			tags
		)

		viewModelScope.launch(Dispatchers.IO) {
			when(uploadTrackUseCase(track, uri)) {
				UploadTrackRC.OK -> _finish.postValue(Unit)
				UploadTrackRC.NOT_CONNECTED -> _errorFlow.emit("Not available in offline mode")
				UploadTrackRC.SERVER_ERROR -> _errorFlow.emit("Unknown server error")
			}
		}
	}
}