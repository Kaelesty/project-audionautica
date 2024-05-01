package com.kaelesty.audionautica.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.domain.usecases.GetUserInfoUseCase
import com.kaelesty.audionautica.domain.usecases.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
	private val logoutUseCase: LogoutUseCase,
	private val getUserInfoUseCase: GetUserInfoUseCase,
): ViewModel() {

	private val _stateFlow = MutableStateFlow<SettingsState>(SettingsState.Loading)
	val stateFlow: StateFlow<SettingsState> get() = _stateFlow.asStateFlow()

	sealed class SettingsState {

		object Loading: SettingsState()
		class UserLoaded(val username: String, val email: String): SettingsState()
	}

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
}