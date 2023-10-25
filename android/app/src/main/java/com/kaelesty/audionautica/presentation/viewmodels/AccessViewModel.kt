package com.kaelesty.audionautica.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccessViewModel: ViewModel() {

	private val _runMusicActivity = MutableLiveData<Unit>()
	val runMusicActivity: LiveData<Unit> get() = _runMusicActivity

	fun register(parameters: List<String>) {
		for (param in parameters) {
			Log.d("AccessViewModel", param)
		}
		_runMusicActivity.value = Unit
	}

	fun signin(parameters: List<String>) {
		for (param in parameters) {
			Log.d("AccessViewModel", param)
		}
		_runMusicActivity.value = Unit
	}

	fun resetPassword(parameters: List<String>) {
		for (param in parameters) {
			Log.d("AccessViewModel", param)
		}
		_runMusicActivity.value = Unit
	}
}