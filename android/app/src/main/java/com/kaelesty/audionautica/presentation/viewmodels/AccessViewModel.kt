package com.kaelesty.audionautica.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.data.repos.AccessRepo
import com.kaelesty.audionautica.domain.usecases.LoginUseCase
import com.kaelesty.audionautica.domain.usecases.RegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccessViewModel: ViewModel() {

	private val _runMusicActivity = MutableLiveData<Unit>()
	val runMusicActivity: LiveData<Unit> get() = _runMusicActivity
	
	private val repo = AccessRepo()
	
	val loginUseCase = LoginUseCase(repo)
	val registerUseCase = RegisterUseCase(repo)

	fun register(parameters: Map<String, String>) {
		//TODO("Attempt to usecase")
		
		_runMusicActivity.value = Unit
	}

	fun signin(parameters: Map<String, String>) {

		//TODO("Attempt to usecase")
		_runMusicActivity.value = Unit
	}

	fun resetPassword(parameters: Map<String, String>) {
		//TODO("Attempt to usecase")
		_runMusicActivity.value = Unit
	}
}