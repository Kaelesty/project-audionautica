package com.kaelesty.audionautica.presentation.composables.dialogues

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme


@Composable
fun ConfirmTrackDeleteDialog(
	onAcceptRequest: (Track) -> Unit,
	onDismissRequest: () -> Unit,
	track: Track
	) {
	Dialog({ onDismissRequest() }) {

		Card(
			modifier = Modifier
				.fillMaxWidth()
				.height(200.dp)
				.padding(16.dp),
			shape = RoundedCornerShape(16.dp),
		) {
			Text(
				text = "Delete ${track.title}?",
				fontSize = 24.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
					.padding(top = 8.dp)
			)
			OutlinedButton(
				onClick = { onAcceptRequest(track) },
				modifier = Modifier
					.fillMaxWidth()
					.padding(12.dp)
			) {
				Text(
					text = "DELETE",
					fontSize = 18.sp,
					fontStyle = FontStyle.Normal,
					fontFamily = SpaceGrotesk,
					fontWeight = FontWeight.Light,
					color = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier
						.padding(horizontal = 10.dp)
						.padding(top = 8.dp, bottom = 8.dp)
				)
			}
			Text(
				text = "* the track will be deleted from the device and from all playlists",
				fontSize = 14.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.Light,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
					.padding(top = 8.dp, bottom = 8.dp)
			)
		}
	}
}