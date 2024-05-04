package com.kaelesty.audionautica.presentation.playlists.addtracktoplaylist

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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.presentation.getFont
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.viewmodels.AccessViewModel_Factory
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory

@Composable
fun AddTrackToPlalistDialog(
	onDismissRequest: () -> Unit,
	track: Track,
	viewmodelFactory: ViewModelFactory
) {

	val vm: AddTrackToPlaylistViewModel = viewModel(factory = viewmodelFactory)

	val playlists by vm.playlistCollection.observeAsState(listOf())

	Dialog(
		onDismissRequest = { onDismissRequest() }
	) {

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
				fontFamily = getFont(),
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
					itemsIndexed(playlists) { index, item ->
						Row(
							modifier = Modifier
								.clickable {
									vm.addTrackToPlaylist(
										trackId = track.id,
										playlistId = item.id
									)
								}
						) {
							if (track.id in item.trackIds) {
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
								fontFamily = getFont(),
								fontWeight = FontWeight.SemiBold,
								color = MaterialTheme.colorScheme.onSurface,
							)
						}
						Spacer(modifier = Modifier.height(4.dp))
					}
				}
			)
			Spacer(modifier = Modifier.height(8.dp))
		}
	}
}
//
//@Preview
//@Composable
//fun previewSelectPlaylistDialog() {
//	AudionauticaTheme {
//		AddTrackToPlalistDialog(
//			onDimissRequest = {},
//			onAcceptRequest = {},
//			playlists = listOf(
//				Playlist(0, "Favorites", listOf()),
//				Playlist(1, "Playlist 1", listOf()),
//				Playlist(2, "Playlist 2", listOf()),
//			)
//		)
//	}
//}