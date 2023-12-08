package com.kaelesty.audionautica.presentation.composables.dialogues

import android.util.Log
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.Tracks
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import kotlinx.coroutines.flow.SharedFlow


@Composable
fun EditPlaylistDialog(
	onDismissRequest: () -> Unit,
	onDeleteTrackFromPlaylist: (Track, Int) -> Unit,
	playlist: Playlist,
	playlistTracks: List<Track>,

	onDeletePlaylist: (Int) -> Unit
) {

	val states = mutableListOf<MutableState<Track?>>()

	// are buttons yes/no visible?
	var deletePlaylistRequest by rememberSaveable {
		mutableStateOf(false)
	}

	playlistTracks.forEach {
		states.add(
			remember {
				mutableStateOf(it)
			}
		)
	}

	Dialog(
		onDismissRequest = { onDismissRequest() }
	) {
		Card(
			modifier = Modifier
				.width(800.dp)
				.height(400.dp)
				.padding(16.dp),
			shape = RoundedCornerShape(16.dp),
		) {
			Text(
				text = "Edit ${playlist.title}",
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
					.padding(top = 8.dp)
					.weight(1f),
				content = {
					itemsIndexed(states) { index, itemNullable ->
						itemNullable.value?.let { item ->
							Card(
								colors = CardDefaults.cardColors(
									containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
										alpha = 0.7f
									)
								),
								modifier = Modifier
									.fillMaxWidth()
									.padding(8.dp)
							) {
								Row(
									modifier = Modifier
										.padding(8.dp),
									verticalAlignment = Alignment.CenterVertically
								) {
									Text(
										text = item.title,
										fontFamily = SpaceGrotesk,
										fontSize = 22.sp,
										modifier = Modifier
											.weight(1f)
									)
									Icon(
										imageVector = Icons.Filled.Close,
										contentDescription = "Play",
										modifier = Modifier
											.size(30.dp)
											.clickable {
												onDeleteTrackFromPlaylist(
													item, playlist.id
												)
												states[index].value = null
											}
									)
								}
							}
						}
					}
				}
			)
//			Row(
//				modifier = Modifier
//					.fillMaxWidth(),
//				verticalAlignment = Alignment.CenterVertically,
//			) {
//				Spacer(Modifier.weight(1f))
//				Text(
//					text = "OK",
//					fontSize = 20.sp,
//					fontStyle = FontStyle.Normal,
//					fontFamily = SpaceGrotesk,
//					fontWeight = FontWeight.SemiBold,
//					color = MaterialTheme.colorScheme.onSurface,
//					modifier = Modifier
//						.padding(horizontal = 14.dp)
//						.padding(top = 8.dp)
//						.clickable {
//							onAcceptRequest(playlist, playlistTracks)
//						}
//				)
//			}
			if (!deletePlaylistRequest) {
				OutlinedButton(
					onClick = { deletePlaylistRequest = true },
					modifier = Modifier
						.fillMaxWidth()
						.padding(12.dp)
				) {
					Text(
						text = "Delete playlist",
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
			} else {
				Row(
					modifier = Modifier.fillMaxWidth().padding(12.dp)
				) {
					OutlinedButton(
						onClick = {
							onDeletePlaylist(playlist.id)
							onDismissRequest()
						},
						modifier = Modifier
							.weight(0.5f)
							.padding(12.dp)
					) {
						Text(
							text = "DELETE",
							fontSize = 18.sp,
							fontStyle = FontStyle.Normal,
							fontFamily = SpaceGrotesk,
							fontWeight = FontWeight.Light,
							color = MaterialTheme.colorScheme.onSurface,
						)
					}
					OutlinedButton(
						onClick = {
							deletePlaylistRequest = false
						},
						modifier = Modifier
							.weight(0.5f)
							.padding(12.dp)
					) {
						Text(
							text = "CANCEL",
							fontSize = 18.sp,
							fontStyle = FontStyle.Normal,
							fontFamily = SpaceGrotesk,
							fontWeight = FontWeight.Light,
							color = MaterialTheme.colorScheme.onSurface,
						)
					}
				}
			}
			Text(
				text = "* changes are saved automatically",
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

//@Preview
//@Composable
//fun previewEditPlaylistDialog() {
//	AudionauticaTheme {
//		EditPlaylistDialog(
//			onDismissRequest = {},
//			onAcceptRequest = { pl, tr ->},
//			playlist = Playlist(0, "Favorites", listOf()),
//			playlistTracks = listOf(
//				Track(0, "title", "artist", listOf("tag1", "tag2")),
//				Track(0, "title", "artist", listOf("tag1", "tag2")),
//				Track(0, "title", "artist", listOf("tag1", "tag2")),
//			)
//		)
//	}
//}