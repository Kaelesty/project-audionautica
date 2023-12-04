package com.kaelesty.audionautica.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme

@Composable
fun AddTrackToPlalistDialog(
	onDimissRequest: () -> Unit,
	onAcceptRequest: (Int) -> Unit,
	playlists: List<Playlist>
) {
	Dialog({}) {

		var selectedPosition by rememberSaveable {
			mutableIntStateOf(0)
		}

		Card(
			modifier = Modifier
				.width(800.dp)
				.height(250.dp)
				.padding(16.dp),
			shape = RoundedCornerShape(16.dp),
		) {
			Text(
				text = "Select Playlist",
				fontSize = 24.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
					.padding(top = 8.dp)
			)
			LazyColumn(
				modifier = Modifier
					.padding(horizontal = 10.dp)
					.padding(top = 8.dp),
				content = {
					itemsIndexed(playlists) { index, item ->
						Row(
							modifier = Modifier
								.clickable {
									selectedPosition = index
								}
						) {
							if (selectedPosition == index) {
								Icon(
									imageVector = Icons.Filled.Check,
									contentDescription = "Selected",
									modifier = Modifier
										.size(20.dp)
								)
								Spacer(Modifier.width(8.dp))
							}
							Text(
								text = item.title,
								fontSize = 20.sp,
								fontStyle = FontStyle.Normal,
								fontFamily = SpaceGrotesk,
								fontWeight = FontWeight.SemiBold,
								color = MaterialTheme.colorScheme.onSurface,
							)
						}
						Spacer(modifier = Modifier.height(4.dp))
					}
				}
			)
			Spacer(modifier = Modifier.height(8.dp))
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 10.dp)
					.padding(top = 8.dp)
			) {
				Spacer(modifier = Modifier.weight(1f))
				Text(
					modifier = Modifier
						.padding(end = 12.dp)
						.clickable {
							onAcceptRequest(
								playlists[selectedPosition].id
							)
						},
					text = "OK",
					textAlign = TextAlign.End,
					fontSize = 20.sp,
					fontFamily = SpaceGrotesk,
				)
			}
		}
	}
}

@Preview
@Composable
fun previewSelectPlaylistDialog() {
	AudionauticaTheme {
		AddTrackToPlalistDialog(
			onDimissRequest = {},
			onAcceptRequest = {},
			playlists = listOf(
				Playlist(0, "Favorites", listOf()),
				Playlist(1, "Playlist 1", listOf()),
				Playlist(2, "Playlist 2", listOf()),
			)
		)
	}
}