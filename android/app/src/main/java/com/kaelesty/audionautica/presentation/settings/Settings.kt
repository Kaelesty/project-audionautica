package com.kaelesty.audionautica.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.getFont
import com.kaelesty.audionautica.presentation.shimmering.shimmerLoadingAnimation
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory

@Composable
fun Settings(
	isOffline: Boolean,
	viewModelFactory: ViewModelFactory
) {

	val vm: SettingsViewModel = viewModel(factory = viewModelFactory)

	val userEmail = vm.getUserEmail()

	LaunchedEffect(userEmail) {
		vm.loadUser()
	}

	val state by vm.stateFlow.collectAsState()

	val username: String?
	val email: String?

	when (state) {
		is SettingsViewModel.SettingsState.Loading -> {
			username = null
			email = null
		}
		is SettingsViewModel.SettingsState.UserLoaded -> {
			username = (state as SettingsViewModel.SettingsState.UserLoaded).username
			email = (state as SettingsViewModel.SettingsState.UserLoaded).email
		}
	}

	var userParamsModifier = Modifier
		.padding(horizontal = 10.dp)
		.shimmerLoadingAnimation(isLoadingCompleted = (username != null && email != null))

	GradientCard {
		Column(
			modifier = Modifier
				.padding(6.dp)
		) {
			if (!isOffline) {
				Text(
					text = "User",
					fontSize = 24.sp,
					fontStyle = FontStyle.Normal,
					fontFamily = getFont(),
					fontWeight = FontWeight.SemiBold,
					color = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier
						.padding(horizontal = 10.dp)
				)
				Spacer(modifier = Modifier.height(6.dp))
				Text(
					text = "Username: ${username ?: "..."}",
					fontSize = 18.sp,
					fontStyle = FontStyle.Normal,
					fontFamily = getFont(),
					modifier = userParamsModifier
				)
				Spacer(modifier = Modifier.height(4.dp))
				Text(
					text = "Email: ${email ?: "..."}",
					fontSize = 18.sp,
					fontStyle = FontStyle.Normal,
					fontFamily = getFont(),
					modifier = userParamsModifier
				)
				Spacer(modifier = Modifier.height(6.dp))
				Button(
					onClick = { vm.logout() },
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 60.dp)
					,
					shape = RoundedCornerShape(8.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.primaryContainer,
						contentColor = MaterialTheme.colorScheme.surface
					)
				) {
					Text(
						"Logout",
						color = MaterialTheme.colorScheme.onSurface,
						fontFamily = getFont()
					)
				}
			}
			else {
				Text(
					text = "Offline settings",
					fontSize = 24.sp,
					fontStyle = FontStyle.Normal,
					fontFamily = getFont(),
					fontWeight = FontWeight.SemiBold,
					color = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier
						.padding(horizontal = 10.dp)
				)
				Spacer(modifier = Modifier.height(6.dp))
				Button(
					onClick = { vm.tryReconnect() },
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 60.dp)
					,
					shape = RoundedCornerShape(8.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.primaryContainer,
						contentColor = MaterialTheme.colorScheme.surface
					)
				) {
					Text(
						"Try reconnecting",
						color = MaterialTheme.colorScheme.onSurface,
						fontFamily = getFont()
					)
				}
			}
		}
	}
}