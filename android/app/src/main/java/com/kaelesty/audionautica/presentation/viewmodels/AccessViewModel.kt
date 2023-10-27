package com.kaelesty.audionautica.presentation.viewmodels

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
import javax.inject.Inject

class AccessViewModel @Inject constructor(
	var loginUseCase: LoginUseCase,
	var registerUseCase: RegisterUseCase
): ViewModel() {

	enum class RunMusicActivityMode {
		ONLINE, OFFLINE
	}

	private val _runMusicActivity = MutableLiveData<RunMusicActivityMode>()
	val runMusicActivity: LiveData<RunMusicActivityMode> get() = _runMusicActivity

	private val _registerError = MutableLiveData<String>()
	val registerError: LiveData<String> get() = _registerError

	private val _loginError = MutableLiveData<String>()
	val loginError: LiveData<String> get() = _loginError

	fun register(parameters: Map<String, String>) {
		viewModelScope.launch(Dispatchers.IO) {
			val result = registerUseCase(
				parameters["Email"] ?: throw IllegalStateException(),
				parameters["Name"] ?: throw IllegalStateException(),
				parameters["Password"] ?: throw IllegalStateException()
			)
			when (result) {
				RegisterRC.OK -> _runMusicActivity.postValue(RunMusicActivityMode.ONLINE)
				RegisterRC.BAD_EMAIL -> _registerError.postValue("Email already registered")
				RegisterRC.UNKNOWN -> _registerError.postValue("Server error")
				RegisterRC.AUTOLOGIN_FAILED -> _registerError.postValue("Registration was successful, but unable to log in")
				RegisterRC.AUTOLOGIN_FATAL_ERROR -> _registerError.postValue("Critical server error")
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
				LoginRC.OK -> _runMusicActivity.postValue(RunMusicActivityMode.ONLINE)
				LoginRC.BAD_LOGIN-> _loginError.postValue("Email not found")
				LoginRC.BAD_PASSWORD -> _loginError.postValue("Wrong password")
				LoginRC.UNKNOWN -> _loginError.postValue("Server error")
			}
		}
	}

	fun continueOffline() {
		_runMusicActivity.postValue(RunMusicActivityMode.OFFLINE)
	}
}