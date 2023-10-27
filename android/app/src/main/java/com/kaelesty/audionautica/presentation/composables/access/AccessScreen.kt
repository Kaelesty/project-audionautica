package com.kaelesty.audionautica.presentation.composables.access

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.viewmodels.AccessViewModel

@Composable
fun AccessScreen(
	viewModel: AccessViewModel
) {

	val accessScreenMode = rememberSaveable {
		mutableStateOf(AccessScreenMode.REGISTER)
	}

	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Image(
			painter = painterResource(id = R.drawable.space_background),
			contentDescription = "Background",
			contentScale = ContentScale.FillBounds,
			modifier = Modifier
				.fillMaxSize()
		)

		when(accessScreenMode.value) {
			AccessScreenMode.REGISTER -> Register(accessScreenMode, viewModel)
			AccessScreenMode.LOGIN -> Login(accessScreenMode, viewModel)
		}
	}
}

@Composable
fun Register(
	controllerState: MutableState<AccessScreenMode>,
	viewModel: AccessViewModel
) {
	var error = viewModel.registerError.observeAsState("")

	MultiparameterInputCard(
		title = "Register",
		mainAction = CollectNamedAction(
			name = "Register",
			onCollect = {
				viewModel.register(it)
			}
		),
		leftAction = SimpleNamedAction(
			name = "Sing in",
			onAction = {
				controllerState.value = AccessScreenMode.LOGIN
			}
		),
		rightAction = SimpleNamedAction(
			name = "Continue offline",
			onAction = {
				// viewModel.continueOffline()
			},
		),
		parameters = listOf("Email", "Name", "Password"),
		errorMessage = error.value
	)
}

@Composable
fun Login(
	controllerState: MutableState<AccessScreenMode>,
	viewModel: AccessViewModel
) {
	val error = viewModel.loginError.observeAsState("")

	MultiparameterInputCard(
		title = "Sing in",
		mainAction = CollectNamedAction(
			name = "Sign in",
			onCollect = {
				viewModel.signin(it)
			}
		),
		leftAction = SimpleNamedAction(
			name = "No Account?",
			onAction = {
				controllerState.value = AccessScreenMode.REGISTER
			}
		),
		rightAction = SimpleNamedAction(
			name = "Continue offline",
			onAction = {
				//viewModel.continueOffline()
			},
		),
		parameters = listOf("Email", "Password"),
		errorMessage = error.value
	)
}


@Preview
@Composable
fun PreviewLight() {
	AudionauticaTheme(darkTheme = false) {
		AccessScreen(AccessViewModel())
	}
}

@Preview
@Composable
fun PreviewDark() {
	AudionauticaTheme(darkTheme = true) {
		AccessScreen(AccessViewModel())
	}
}