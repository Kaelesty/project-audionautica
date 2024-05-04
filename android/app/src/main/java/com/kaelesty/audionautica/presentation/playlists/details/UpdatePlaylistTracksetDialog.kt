package com.kaelesty.audionautica.presentation.playlists.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.presentation.getFont

@Composable
fun UpdatePlaylistTracksetDialog(
	onSubmit: (List<Int>) -> Unit,
	onDismiss: () -> Unit,
	tracks: List<Track>,
	playlist: UserPlaylist.Entire
) {

	var tracksetState by rememberSaveable {
		mutableStateOf(tracks)
	}

	Dialog(
		onDismissRequest = { onDismiss() }
	) {
		Card(
			modifier = Modifier
				.width(800.dp)
				.height(600.dp)
				.padding(16.dp),
			shape = RoundedCornerShape(16.dp),
		) {
			Text(
				text = "Edit tracks order in ${playlist.title}",
				fontSize = 24.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = getFont(),
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
					.padding(top = 8.dp)
			)
			Spacer(modifier = Modifier.height(4.dp))
			LazyColumn(
				modifier = Modifier
					.padding(horizontal = 10.dp)
					.padding(top = 8.dp)
					.weight(1f)
			) {
				itemsIndexed(tracksetState) { index, track ->
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.padding(vertical = 4.dp)
					) {
						Text(
							text = "$index ${track.title} - ${track.artist}",
							fontSize = 18.sp,
							fontStyle = FontStyle.Normal,
							fontFamily = getFont(),
							fontWeight = FontWeight.Thin,
							modifier = Modifier
								.padding(horizontal = 10.dp)
								.weight(1f)
						)
						if (index != 0) {
							Icon(
								imageVector = Icons.Filled.KeyboardArrowUp,
								contentDescription = null,
								modifier = Modifier
									.clickable {
										val copy = tracksetState.toMutableList()
										copy.removeAt(index)
										copy.add(index - 1, track)
										tracksetState = copy
									}
									.size(30.dp)
							)
						}
						if (index != tracksetState.size - 1) {
							Icon(
								imageVector = Icons.Filled.KeyboardArrowDown,
								contentDescription = null,
								modifier = Modifier
									.clickable {
										val copy = tracksetState.toMutableList()
										copy.removeAt(index)
										copy.add(index + 1, track)
										tracksetState = copy
									}
									.size(30.dp)
							)
						}
						Icon(
							imageVector = Icons.Filled.Clear,
							contentDescription = null,
							modifier = Modifier
								.clickable {
									val copy = tracksetState.toMutableList()
									copy.removeAt(index)
									tracksetState = copy
								}
								.size(30.dp)
						)
					}
				}
			}
			Spacer(modifier = Modifier.height(4.dp))
			Button(
				onClick = {
					onSubmit(tracksetState.map { it.id })
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 60.dp),
				shape = RoundedCornerShape(8.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					contentColor = MaterialTheme.colorScheme.surface
				)
			) {
				Text(
					"Save",
					color = MaterialTheme.colorScheme.onSurface,
					fontFamily = getFont()
				)
			}
			Spacer(modifier = Modifier.height(4.dp))
		}
	}
}