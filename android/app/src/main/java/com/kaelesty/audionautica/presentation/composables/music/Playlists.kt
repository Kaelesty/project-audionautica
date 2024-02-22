package com.kaelesty.audionautica.presentation.composables.music

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk

@Composable
fun Playlists(
	playlistsLiveData: LiveData<List<Playlist>>,
	onPlayPlaylist: (Int) -> Unit = {},
	editPlaylistDialog: MutableState<Playlist?>,
	createPlaylistDialog: MutableState<Unit?>,
) {
	val playlists by playlistsLiveData.observeAsState(listOf())

	LazyColumn(
		modifier = Modifier
			.padding(12.dp)
			.fillMaxHeight(),
		content = {
			itemsIndexed(playlists) { index, item ->
				Card(
					colors = CardDefaults.cardColors(
						containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
					),
					modifier = Modifier
						.fillMaxWidth()
						.padding(8.dp)
						.clickable {
							onPlayPlaylist(item.id)
						}
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
							imageVector = Icons.Filled.Edit,
							contentDescription = "Edit",
							modifier = Modifier
								.size(30.dp)
								.clickable {
									editPlaylistDialog.value = item
								}
						)
					}
				}
			}
		}
	)
	Row(
		modifier = Modifier
			.fillMaxSize()
			.padding(bottom = 192.dp, end = 12.dp)
		// 192.dp to make position equals with button of MyTracks
		,
		verticalAlignment = Alignment.Bottom
	) {
		Spacer(Modifier.weight(1f))
		IconButton(
			onClick = {
				createPlaylistDialog.value = Unit
			},
			modifier = Modifier
				.background(
					color = MaterialTheme.colorScheme.surface,
					shape = RoundedCornerShape(4.dp)
				)

		) {
			Icon(
				imageVector = Icons.Filled.Add,
				contentDescription = null
			)
		}
	}
}