package com.kaelesty.audionautica.presentation.composables.access

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.presentation.composables.dialogues.LoadingDialog
import com.kaelesty.audionautica.presentation.getFont
import com.kaelesty.audionautica.presentation.viewmodels.AccessViewModel
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory

@Composable
fun AccessScreen(
	viewModelFactory: ViewModelFactory,
	onFinish: (AccessViewModel.RunMusicActivityMode) -> Unit
) {

	val accessScreenMode = rememberSaveable {
		mutableStateOf(AccessScreenMode.LOGIN)
	}

	val vm = viewModel<AccessViewModel>(factory = viewModelFactory)


	val state by vm.stateFlow.collectAsState()

	when (state) {
		is AccessViewModel.AccessScreenState.Initial -> {
			vm.checkConnectionAndAuth(
				(state as AccessViewModel.AccessScreenState.Initial).autoAuth
			)
		}

		is AccessViewModel.AccessScreenState.Loading -> {
			LoadingDialog()
		}

		is AccessViewModel.AccessScreenState.Finish -> {
			onFinish((state as AccessViewModel.AccessScreenState.Finish).runMode)
			vm.resetAfterFinish() // reset state to normal to avoid instant finish after logout
		}

		is AccessViewModel.AccessScreenState.Normal -> {
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
				val error = (state as AccessViewModel.AccessScreenState.Normal).errorMessage
				when (accessScreenMode.value) {
					AccessScreenMode.REGISTER -> Register(accessScreenMode, vm, error)
					AccessScreenMode.LOGIN -> Login(accessScreenMode, vm, error)
				}
			}
		}

		is AccessViewModel.AccessScreenState.ChooseLogin -> {
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
				ChooseLogin(
					vm,
					(state as AccessViewModel.AccessScreenState.ChooseLogin).logins,
					(state as AccessViewModel.AccessScreenState.ChooseLogin).errorMessage
				)
			}
		}
	}

}

@Composable
fun ChooseLogin(
	vm: AccessViewModel,
	logins: List<String>,
	errorMessage: String?,
) {
	GradientCard {
		Column(
			modifier = Modifier
				.padding(6.dp)
		) {
			Text(
				text = "Choose account to autologin",
				fontSize = 24.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = getFont(),
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
			)
			LazyColumn(
				modifier = Modifier
					.padding(horizontal = 10.dp)
			) {
				items(logins, key = { it }) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.clickable {
								vm.autoLogin(it)
							}
					) {
						Text(
							text = it,
							fontSize = 24.sp,
							fontStyle = FontStyle.Normal,
							fontFamily = getFont(),
							fontWeight = FontWeight.SemiBold,
							modifier = Modifier.weight(1f)
							)
						Icon(
							imageVector = Icons.Filled.Clear,
							contentDescription = "delete account",
							modifier = Modifier
								.size(44.dp)
								.clickable {
									vm.deleteLogin(it)
								}
						)
					}
				}
			}
			Spacer(Modifier.height(4.dp))
			errorMessage?.let {
				Spacer(Modifier.height(6.dp))
				Text(
					text = it,
					color = Color.Red,
					fontSize = 16.sp,
					fontFamily = getFont(),
					modifier = Modifier
						.padding(horizontal = 12.dp)
				)
			}
			Spacer(Modifier.height(4.dp))
			Row {
				Button(
					onClick = {
						vm.runOfflineMode()
					},
					modifier = Modifier
						.weight(1f)
						.padding(horizontal = 6.dp),
					shape = RoundedCornerShape(8.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.primaryContainer,
						contentColor = MaterialTheme.colorScheme.surface
					)
				) {
					Text(
						"Continue offline",
						color = MaterialTheme.colorScheme.onSurface,
						fontFamily = getFont()
					)
				}
				Button(
					onClick = {
						vm.toDefaultAuth()
					},
					modifier = Modifier
						.weight(1f)
						.padding(horizontal = 6.dp),
					shape = RoundedCornerShape(8.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.primaryContainer,
						contentColor = MaterialTheme.colorScheme.surface
					)
				) {
					Text(
						"Default auth",
						color = MaterialTheme.colorScheme.onSurface,
						fontFamily = getFont()
					)
				}
			}
		}
	}
}

@Composable
fun Register(
	controllerState: MutableState<AccessScreenMode>,
	viewModel: AccessViewModel,
	error: String,
) {

	MultiparameterInputCard(
		title = "Register",
		mainAction = CollectNamedAction(
			name = "Register",
			onCollect = { params, files ->
				viewModel.register(params)
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
				viewModel.continueOffline()
			},
		),
		parameters = listOf("Email", "Name", "Password"),
		errorMessage = error
	)
}

@Composable
fun Login(
	controllerState: MutableState<AccessScreenMode>,
	viewModel: AccessViewModel,
	error: String,
) {

	MultiparameterInputCard(
		title = "Sing in",
		mainAction = CollectNamedAction(
			name = "Sign in",
			onCollect = { params, files ->
				viewModel.signin(params)
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
				viewModel.continueOffline()
			},
		),
		parameters = listOf("Email", "Password"),
		errorMessage = error
	)
}


//@Preview
//@Composable
//fun PreviewLight() {
//	AudionauticaTheme(darkTheme = false) {
//		AccessScreen(AccessViewModel())
//	}
//}
//
//@Preview
//@Composable
//fun PreviewDark() {
//	AudionauticaTheme(darkTheme = true) {
//		AccessScreen(AccessViewModel())
//	}
//}