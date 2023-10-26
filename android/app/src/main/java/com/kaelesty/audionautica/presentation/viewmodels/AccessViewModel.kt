package com.kaelesty.audionautica.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.data.repos.AccessRepo
import com.kaelesty.audionautica.domain.returncodes.LoginRC
import com.kaelesty.audionautica.domain.returncodes.RegisterRC
import com.kaelesty.audionautica.domain.usecases.LoginUseCase
import com.kaelesty.audionautica.domain.usecases.RegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccessViewModel: ViewModel() {

	private val _runMusicActivity = MutableLiveData<Unit>()
	val runMusicActivity: LiveData<Unit> get() = _runMusicActivity

	private val _registerError = MutableLiveData<String>()
	val registerError: LiveData<String> get() = _registerError

	private val _loginError = MutableLiveData<String>()
	val loginError: LiveData<String> get() = _loginError
	
	private val repo = AccessRepo()
	
	val loginUseCase = LoginUseCase(repo)
	val registerUseCase = RegisterUseCase(repo)

	fun register(parameters: Map<String, String>) {
		viewModelScope.launch(Dispatchers.IO) {
			val result = registerUseCase(
				parameters["Email"] ?: throw IllegalStateException(),
				parameters["Name"] ?: throw IllegalStateException(),
				parameters["Password"] ?: throw IllegalStateException()
			)
			when (result) {
				RegisterRC.OK -> _runMusicActivity.postValue(Unit)
				RegisterRC.BAD_EMAIL -> _registerError.postValue("Email already registered")
				RegisterRC.UNKNOWN -> _registerError.postValue("Server error")
			}
		}
	}

	fun signin(parameters: Map<String, String>) {
		viewModelScope.launch(Dispatchers.IO) {
			val result = loginUseCase(
				parameters["Email"] ?: throw IllegalStateException(),
				parameters["Password"] ?: throw IllegalStateException()
			)

			when (result) {
				LoginRC.OK -> _runMusicActivity.postValue(Unit)
				LoginRC.BAD_LOGIN-> _loginError.postValue("Email not found")
				LoginRC.BAD_PASSWORD -> _loginError.postValue("Wrong password")
				LoginRC.UNKNOWN -> _loginError.postValue("Server error")
			}
		}
	}

	fun resetPassword(parameters: Map<String, String>) {
		//TODO("Attempt to usecase")
		_runMusicActivity.value = Unit
	}
}