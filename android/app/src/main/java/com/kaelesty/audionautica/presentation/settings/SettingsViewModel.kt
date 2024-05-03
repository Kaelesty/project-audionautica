package com.kaelesty.audionautica.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.returncodes.CheckConnectionRC
import com.kaelesty.audionautica.domain.usecases.CheckConnectionUseCase
import com.kaelesty.audionautica.domain.usecases.GetUserEmailUseCase
import com.kaelesty.audionautica.domain.usecases.GetUserInfoUseCase
import com.kaelesty.audionautica.domain.usecases.LogoutUseCase
import com.kaelesty.audionautica.presentation.toastmanager.ToastManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
	private val logoutUseCase: LogoutUseCase,
	private val getUserInfoUseCase: GetUserInfoUseCase,

	private val toastManager: ToastManager,
	private val checkConnectionUseCase: CheckConnectionUseCase,
	private val getUserEmailUseCase: GetUserEmailUseCase
): ViewModel() {

	private val _stateFlow = MutableStateFlow<SettingsState>(SettingsState.Loading)
	val stateFlow: StateFlow<SettingsState> get() = _stateFlow.asStateFlow()

	sealed class SettingsState {

		object Loading: SettingsState()
		class UserLoaded(val username: String, val email: String): SettingsState()
	}

	fun getUserEmail() = getUserEmailUseCase()

	fun logout() {
		viewModelScope.launch(Dispatchers.IO) {
			logoutUseCase() // Logout calls a callback in MusicActivity. There's no need to process his work here
		}
	}

	fun loadUser() {
		viewModelScope.launch(Dispatchers.IO) {
			getUserInfoUseCase()?.let {
				_stateFlow.emit(
					SettingsState.UserLoaded(
						it.second, it.first
					)
				)
			}
		}
	}

	fun tryReconnect() {
		viewModelScope.launch(Dispatchers.Main) {
			if (checkConnectionUseCase() == CheckConnectionRC.OK) {
				logoutUseCase()
			}
			else {
				toastManager.showToast("Reconnection failed")
			}
		}
	}
}