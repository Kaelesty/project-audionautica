package com.kaelesty.audionautica.presentation.playlists.details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.presentation.getFont
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk

@Composable
fun ConfirmPlaylistDeletionDialog(
	onAccept: (Int) -> Unit,
	onDismiss: () -> Unit,
	playlist: UserPlaylist.Entire
) {

	Dialog({ onDismiss() }) {

		Card(
			modifier = Modifier
				.fillMaxWidth()
				.height(200.dp)
				.padding(16.dp),
			shape = RoundedCornerShape(16.dp),
		) {
			Text(
				text = "Delete ${playlist.title}?",
				fontSize = 24.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = getFont(),
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
					.padding(top = 8.dp)
			)
			OutlinedButton(
				onClick = { onAccept(playlist.id) },
				modifier = Modifier
					.fillMaxWidth()
					.padding(12.dp)
			) {
				Text(
					text = "DELETE",
					fontSize = 18.sp,
					fontStyle = FontStyle.Normal,
					fontFamily = getFont(),
					fontWeight = FontWeight.Light,
					color = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier
						.padding(horizontal = 10.dp)
						.padding(top = 8.dp, bottom = 8.dp)
				)
			}
			Text(
				text = "* this action cannot be undone",
				fontSize = 14.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = getFont(),
				fontWeight = FontWeight.Light,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
					.padding(top = 8.dp, bottom = 8.dp)
			)
		}
	}
}