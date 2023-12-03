package com.kaelesty.audionautica.presentation.composables.addtrack

import android.net.Uri
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.kaelesty.audionautica.presentation.activities.AddTrackActivity
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme

@Preview
@Composable
fun PreviewLightChooseMusicFileDialog() {
	AudionauticaTheme(darkTheme = false) {
		ChooseMusicFileDialog(
			options = listOf(
				AddTrackActivity.NamedUri("Track1", Uri.EMPTY),
				AddTrackActivity.NamedUri("Track2", Uri.EMPTY),
				AddTrackActivity.NamedUri("Track3", Uri.EMPTY),
			),
			onClose = {}
		)
	}
}

@Preview
@Composable
fun PreviewDarkChooseMusicFileDialog() {
	AudionauticaTheme(darkTheme = true) {
		ChooseMusicFileDialog(
			options = listOf(
				AddTrackActivity.NamedUri("Track1", Uri.EMPTY),
				AddTrackActivity.NamedUri("Track2", Uri.EMPTY),
				AddTrackActivity.NamedUri("Track3", Uri.EMPTY),
			),
			onClose = {}
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseMusicFileDialog(
	options: List<AddTrackActivity.NamedUri>,
	onClose: (AddTrackActivity.NamedUri) -> Unit
) {
	val openDialog by rememberSaveable {
		mutableStateOf(true)
	}

	if (openDialog) {
		AlertDialog(
			onDismissRequest = { },
		) {
			Text("Choose track")
		}
	}

}