package com.kaelesty.audionautica.presentation.composables.addtrack

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.presentation.activities.AddTrackActivity
import com.kaelesty.audionautica.presentation.composables.access.CollectNamedAction
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.composables.access.MultiparameterInputCard
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.viewmodels.AddTrackViewModel
import java.io.File

@Composable
fun AddTrackScreen(
	viewModel: AddTrackViewModel,
	fileBrowser: (AddTrackActivity.FilesToBrowse) -> Unit,
) {
	val error by viewModel.error.observeAsState()
	val musicFile by viewModel.musicFile.observeAsState()
	val posterFile by viewModel.posterFile.observeAsState()

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

		Column {
			MultiparameterInputCard(
				mainAction = CollectNamedAction(
					name = "Upload",
				) { params, files ->
					viewModel.addTrack(
						params["Artist"] ?: return@CollectNamedAction,
						params["Title"] ?: return@CollectNamedAction
					)
				},
				parameters = listOf("Title", "Artist"),
				title = "Add new track",
				errorMessage = error,
				lastParameterIsPassword = false,
			)

			FileBrowser(
				musicFile,
				{ fileBrowser(AddTrackActivity.FilesToBrowse.MUSIC) },
				"Music file"
			)
//			FileBrowser(
//				posterFile,
//				{ fileBrowser(AddTrackActivity.FilesToBrowse.POSTER) },
//				"Poster file"
//			)
		}
	}
}

@Composable
fun FileBrowser(
	uri: Uri?,
	fileBrowser: () -> Unit,
	hint: String
) {
	GradientCard {
		Row(
			Modifier
				.fillMaxWidth()
				.padding(6.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			TextField(
				value = uri?.lastPathSegment ?: "",
				onValueChange = {},
				modifier = Modifier
					.weight(1f)
					.padding(horizontal = 10.dp),
				label = {
					Text(
						hint,
						color = MaterialTheme.colorScheme.onSurface,
						fontFamily = SpaceGrotesk
					)
				},
				singleLine = true,
				readOnly = true
			)
			Button(
				onClick = {
					fileBrowser()
				},
				modifier = Modifier
					.height(50.dp)
					.padding(horizontal = 6.dp),
				shape = RoundedCornerShape(8.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					contentColor = MaterialTheme.colorScheme.surface
				)
			) {
				Text(
					"Browse",
					color = MaterialTheme.colorScheme.onSurface,
					fontFamily = SpaceGrotesk
				)
			}
		}
	}
}