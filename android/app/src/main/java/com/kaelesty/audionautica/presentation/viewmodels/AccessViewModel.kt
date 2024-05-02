package com.kaelesty.audionautica.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.returncodes.CheckAuthRC
import com.kaelesty.audionautica.domain.returncodes.CheckConnectionRC
import com.kaelesty.audionautica.domain.returncodes.LoginRC
import com.kaelesty.audionautica.domain.returncodes.RegisterRC
import com.kaelesty.audionautica.domain.usecases.CheckAuthUseCase
import com.kaelesty.audionautica.domain.usecases.CheckConnectionUseCase
import com.kaelesty.audionautica.domain.usecases.GetLoginsUseCase
import com.kaelesty.audionautica.domain.usecases.GetPasswordUseCase
import com.kaelesty.audionautica.domain.usecases.LoginUseCase
import com.kaelesty.audionautica.domain.usecases.RegisterUseCase
import com.kaelesty.audionautica.domain.usecases.RemoveLoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccessViewModel @Inject constructor(
	private var loginUseCase: LoginUseCase,
	private var registerUseCase: RegisterUseCase,
	private val checkConnectionUseCase: CheckConnectionUseCase,
	private val checkAuthUseCase: CheckAuthUseCase,
	private val getLoginsUseCase: GetLoginsUseCase,
	private val removeLoginUseCase: RemoveLoginUseCase,
	private val getPasswordUseCase: GetPasswordUseCase
): ViewModel() {

	enum class RunMusicActivityMode {
		ONLINE, OFFLINE
	}

	sealed class AccessScreenState {

		object Loading: AccessScreenState()

		data class Finish(val runMode: RunMusicActivityMode): AccessScreenState()

		data class Normal(val errorMessage: String): AccessScreenState()

		data class ChooseLogin(val logins: List<String>, val errorMessage: String? = null): AccessScreenState()
	}

	private val _stateFlow = MutableStateFlow<AccessScreenState>(AccessScreenState.Loading)
	val stateFlow: StateFlow<AccessScreenState> get() = _stateFlow.asStateFlow()

	fun deleteLogin(login: String, message: String? = null) {
		viewModelScope.launch(Dispatchers.IO) {
			removeLoginUseCase(login)
			_stateFlow.emit(
				AccessScreenState.ChooseLogin(
					logins = getLoginsUseCase()
				)
			)
		}
	}

	fun autoLogin(login: String) {
		viewModelScope.launch(Dispatchers.IO) {
			val password = getPasswordUseCase(login)
			val res = loginUseCase(login, password)
			when (res) {
				LoginRC.OK -> {
					_stateFlow.emit(AccessScreenState.Finish(RunMusicActivityMode.ONLINE))
				}
				LoginRC.UNKNOWN -> {
					_stateFlow.emit(
						(_stateFlow.value as AccessScreenState.ChooseLogin).copy(
							errorMessage = "Unknown server error"
						)
					)
				}
				else -> {
					deleteLogin(login, message = "Account information is out of date and has been deleted from local storage")
				}
			}
		}
	}

	fun runOfflineMode() {
		viewModelScope.launch(Dispatchers.IO) {
			_stateFlow.emit(AccessScreenState.Finish(RunMusicActivityMode.OFFLINE))
		}
	}

	fun register(parameters: Map<String, String>) {
		viewModelScope.launch(Dispatchers.IO) {
			_stateFlow.emit(AccessScreenState.Loading)
			val result = registerUseCase(
				parameters["Email"]?.trim() ?: throw IllegalStateException(),
				parameters["Name"]?.trim() ?: throw IllegalStateException(),
				parameters["Password"]?.trim() ?: throw IllegalStateException()
			)
			when (result) {
				RegisterRC.OK -> _stateFlow.emit(AccessScreenState.Finish(RunMusicActivityMode.ONLINE))
				RegisterRC.BAD_EMAIL -> _stateFlow.emit(AccessScreenState.Normal("Email already registered"))
				RegisterRC.UNKNOWN -> _stateFlow.emit(AccessScreenState.Normal("Server error"))
				RegisterRC.AUTOLOGIN_FAILED -> _stateFlow.emit(AccessScreenState.Normal("Registration was successful, but unable to log in"))
				RegisterRC.AUTOLOGIN_FATAL_ERROR -> _stateFlow.emit(AccessScreenState.Normal("Critical server error"))
			}
		}
	}

	fun signin(parameters: Map<String, String>) {
		viewModelScope.launch(Dispatchers.IO) {
			_stateFlow.emit(AccessScreenState.Loading)
			val result = loginUseCase(
				parameters["Email"]?.trim() ?: throw IllegalStateException(),
				parameters["Password"]?.trim() ?: throw IllegalStateException()
			)

			when (result) {
				LoginRC.OK -> _stateFlow.emit(AccessScreenState.Finish(RunMusicActivityMode.ONLINE))
				LoginRC.BAD_LOGIN-> _stateFlow.emit(AccessScreenState.Normal("Email not found"))
				LoginRC.BAD_PASSWORD -> _stateFlow.emit(AccessScreenState.Normal("Wrong password"))
				LoginRC.UNKNOWN -> _stateFlow.emit(AccessScreenState.Normal("Server error"))
			}
		}
	}

	fun continueOffline() {
		viewModelScope.launch(Dispatchers.IO) {
			_stateFlow.emit(AccessScreenState.Finish(RunMusicActivityMode.OFFLINE))
		}
	}

	fun checkConnectionAndAuth() {
		viewModelScope.launch(Dispatchers.IO) {
			_stateFlow.emit(AccessScreenState.Loading)
			val res = checkConnectionUseCase()
			when(res) {
				CheckConnectionRC.NOT_OK -> {
					_stateFlow.emit(AccessScreenState.Finish(RunMusicActivityMode.OFFLINE))
				}
				CheckConnectionRC.OK -> {
					val res = checkAuthUseCase()
					when(res) {
						CheckAuthRC.NOT_OK -> {
							val logins = getLoginsUseCase()
							if (logins.isNotEmpty()) {
								_stateFlow.emit(
									AccessScreenState.ChooseLogin(logins)
								)
							}
							else {
								_stateFlow.emit(
									AccessScreenState.Normal(errorMessage = "")
								)
							}
						}
						CheckAuthRC.OK -> {
							_stateFlow.emit(AccessScreenState.Finish(RunMusicActivityMode.ONLINE))
						}
					}

				}
			}
		}
	}
}