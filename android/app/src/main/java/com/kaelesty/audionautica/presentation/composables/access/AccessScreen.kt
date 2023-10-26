package com.kaelesty.audionautica.presentation.composables.access

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.ui.theme.CardBorderDark
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
			AccessScreenMode.RESET_PASSWORD -> ResetPassword(accessScreenMode, viewModel)
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
			name = "Already have an account?",
			onAction = {
				controllerState.value = AccessScreenMode.LOGIN
			}
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
			name = "Reset Password",
			onAction = {
				controllerState.value = AccessScreenMode.RESET_PASSWORD
			}
		),
		parameters = listOf("Email", "Password"),
		errorMessage = error.value
	)
}

@Composable
fun ResetPassword(
	controllerState: MutableState<AccessScreenMode>,
	viewModel: AccessViewModel
) {
	MultiparameterInputCard(
		title = "Password Reset",
		mainAction = CollectNamedAction(
			name = "RESET",
			onCollect = {
				viewModel.resetPassword(it)
			}
		),
		leftAction = SimpleNamedAction(
			name = "Login",
			onAction = {
				controllerState.value = AccessScreenMode.LOGIN
			}
		),
		rightAction = SimpleNamedAction(
			name = "Register",
			onAction = {
				controllerState.value = AccessScreenMode.REGISTER
			}
		),
		parameters = listOf("Email")
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