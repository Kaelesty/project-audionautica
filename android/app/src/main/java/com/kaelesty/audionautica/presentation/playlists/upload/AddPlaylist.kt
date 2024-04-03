package com.kaelesty.audionautica.presentation.playlists.upload

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.composables.access.TextInput
import com.kaelesty.audionautica.presentation.composables.dialogues.LoadingDialog
import com.kaelesty.audionautica.presentation.playlists.PlaylistsViewModel
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun AddPlaylistScreen(
	viewModelFactory: ViewModelFactory,
	requestImageUri: () -> Unit,
	requestedImageFlow: SharedFlow<Uri?>,
	navigateToListScreen: () -> Unit,
) {

	val viewModel: PlaylistUploadViewModel = viewModel(factory = viewModelFactory)

	val state by viewModel.stateFlow.collectAsState()
	var errorMessage: String? = ""

	when (state) {
		is PlaylistUploadState.Uploading -> {
			LoadingDialog()
		}
		is PlaylistUploadState.Error -> {
			errorMessage = (state as PlaylistUploadState.Error).error
		}
		is PlaylistUploadState.Finished -> {
			navigateToListScreen()
		}
		else -> {}
	}


	var nameValue = rememberSaveable {
		mutableStateOf("")
	}
	var descValue = rememberSaveable {
		mutableStateOf("")
	}

	val imageUri = requestedImageFlow.collectAsState(initial = Uri.EMPTY)

	val uriString = imageUri.value
	var isPrivateValue by rememberSaveable {
		mutableStateOf(false)
	}



	GradientCard {
		Column(
			modifier = Modifier
				.padding(6.dp)
		) {
			Text(
				text = "Create New Playlist",
				fontSize = 24.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
			)
			Spacer(modifier = Modifier.height(12.dp))
			TextInput(
				value = nameValue,
				hint = "Title"
			)
			Spacer(modifier = Modifier.height(12.dp))
			TextInput(
				value = descValue,
				hint = "Description"
			)
			Spacer(modifier = Modifier.height(12.dp))

//			TextInput(
//				value = uriString,
//				hint = "Image",
//				modifier = Modifier.clickable {
//					requestImageUri()
//				},
//				isReadOnly = true,
//			)
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				TextField(
					value = uriString?.encodedPath ?: "none",
					onValueChange = {},
					readOnly = true,
					label = {
						Text(
							"Image",
							color = MaterialTheme.colorScheme.onSurface,
							fontFamily = SpaceGrotesk
						)
					},
					singleLine = true,
					modifier = Modifier
						.padding(horizontal = 10.dp)
						.weight(1f)
				)
				Button(
					onClick = { requestImageUri() },
					modifier = Modifier
						.width(80.dp),
					shape = RoundedCornerShape(8.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.primaryContainer,
						contentColor = MaterialTheme.colorScheme.surface
					)
				) {
					Text(
						"Pick",
						color = MaterialTheme.colorScheme.onSurface,
						fontFamily = SpaceGrotesk
					)
				}
			}
			Spacer(modifier = Modifier.height(8.dp))
			Row(
				Modifier
					.width(150.dp)
					.height(45.dp)
				,
				verticalAlignment = Alignment.CenterVertically
			) {
				Checkbox(checked = isPrivateValue, onCheckedChange = { isPrivateValue = it})
				Text(
					"Is private",
					color = MaterialTheme.colorScheme.onSurface,
					fontFamily = SpaceGrotesk
				)
			}
			errorMessage?.let {
				Spacer(Modifier.height(6.dp))
				Text(
					text = it,
					color = Color.Red,
					fontSize = 16.sp,
					fontFamily = SpaceGrotesk,
					modifier = Modifier
						.padding(horizontal = 12.dp)
				)
			}
			Spacer(modifier = Modifier.height(12.dp))
			Button(
				onClick = {
					viewModel.uploadPlaylist(
						uri = imageUri.value,
						title = nameValue.value,
						desc = descValue.value,
						trackIds = listOf(),
						isPrivate = isPrivateValue
					)
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 90.dp),
				shape = RoundedCornerShape(8.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					contentColor = MaterialTheme.colorScheme.surface
				)
			) {
				Text(
					"Create",
					color = MaterialTheme.colorScheme.onSurface,
					fontFamily = SpaceGrotesk
				)
			}
			Spacer(modifier = Modifier.height(12.dp))
		}
	}
}