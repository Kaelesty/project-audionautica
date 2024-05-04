package com.kaelesty.audionautica.presentation.playlists.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.player.PlayerState
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.presentation.getCurrentPlaylistId
import com.kaelesty.audionautica.presentation.getCurrentTrack
import com.kaelesty.audionautica.presentation.getFont
import com.kaelesty.audionautica.presentation.player.PlayerViewModel
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory


@Composable
fun PlaylistDetails(
	playlistId: Int,
	viewModelFactory: ViewModelFactory
) {

	val vm = viewModel<PlaylistDetailsViewModel>(factory = viewModelFactory)
	val playerViewModel = viewModel<PlayerViewModel>(factory = viewModelFactory)

	LaunchedEffect(key1 = playlistId) {
		vm.setPlaylist(playlistId)
	}

	val playlist by vm.playlist.observeAsState(null)

	var editTracksetDialogState by rememberSaveable {
		mutableStateOf(false)
	}

	if (playlist == null) {
		Loading()
		return
	}

	LaunchedEffect(key1 = (playlist as UserPlaylist.Entire).trackIds.size, key2 = playlistId) {
		vm.loadTracks(playlist as UserPlaylist.Entire)
	}

	val playerState by playerViewModel.playerState.collectAsState()

	val state by vm.stateFlow.collectAsState()

	Column {
		when (state) {
			is PlaylistDetailsViewModel.PlaylistDetailsState.Empty -> {
				Loading()
			}

			is PlaylistDetailsViewModel.PlaylistDetailsState.TracksLoaded -> {
				with(state as PlaylistDetailsViewModel.PlaylistDetailsState.TracksLoaded) {
					TopPlaylistCard(
						playlist = playlist as UserPlaylist.Entire,
						vm = vm,
						playerState = playerState,
						onEnableTracksetEditing = {
							editTracksetDialogState = true
						}
					)
					Tracks(
						tracks = tracks,
						playlist = playlist as UserPlaylist.Entire,
						playerState = playerState,
						vm = vm
					)
					if (editTracksetDialogState) {
						UpdatePlaylistTracksetDialog(
							onSubmit = {
								vm.updatePlaylistTrackset(it)
								editTracksetDialogState = false
							},
							onDismiss = { editTracksetDialogState = false },
							tracks = tracks,
							playlist = playlist as UserPlaylist.Entire
						)
					}
				}

			}

			is PlaylistDetailsViewModel.PlaylistDetailsState.LoadingTracks -> {
				TopPlaylistCard(
					playlist = playlist as UserPlaylist.Entire,
					vm = vm,
					playerState = playerState,
					onEnableTracksetEditing = {
						// nothing, tracks are loading
					}
				)
				Loading()
			}
		}
	}
}

@Composable
fun Tracks(
	tracks: List<Track>,
	playlist: UserPlaylist.Entire,
	playerState: PlayerState,
	vm: PlaylistDetailsViewModel
) {
	Column {


		if (tracks.isEmpty()) {

			if (playlist.trackIds.isNotEmpty()) {
				Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
					Card(
						colors = CardDefaults.cardColors(
							containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
						),
					) {
						CircularProgressIndicator(
							modifier = Modifier.padding(12.dp)
						)
					}
				}
			} else {
				Card(
					colors = CardDefaults.cardColors(
						containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
					),
					modifier = Modifier
						.padding(12.dp)
						.fillMaxWidth()
						.height(100.dp)
				) {
					Column(
						modifier = Modifier
							.padding(16.dp),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							"Playlist is empty...",
							color = MaterialTheme.colorScheme.onSurface,
							fontFamily = getFont(),
							fontSize = 24.sp,
							fontWeight = FontWeight.ExtraBold,
						)
						Text(
							"Try to add some tracks!",
							color = MaterialTheme.colorScheme.onSurface,
							fontFamily = getFont(),
							fontSize = 20.sp,
							fontWeight = FontWeight.Bold,
						)
					}
				}
			}

			return
		}

		LazyColumn {
			itemsIndexed(
				items = tracks,
			) { index, it ->
				Card(
					colors = CardDefaults.cardColors(
						containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
					),
					modifier = Modifier
						.padding(horizontal = 12.dp, vertical = 6.dp)
						.fillMaxWidth()
						.clickable {
							vm.playFromPosition(index)
						}
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						Column(
							Modifier
								.padding(6.dp)
								.weight(1f)
						) {
							Text(
								"${it.title} - ${it.artist}",
								color = MaterialTheme.colorScheme.onSurface,
								fontFamily = getFont(),
								fontSize = 20.sp,
								fontWeight = FontWeight.ExtraBold,
							)
							Row {
								it.tags.forEach { tag ->
									Box(
										modifier = Modifier
											.background(
												color = MaterialTheme.colorScheme.surfaceVariant,
												shape = RoundedCornerShape(10.dp)
											)
									) {
										Text(
											text = tag,
											fontFamily = getFont(),
											fontWeight = FontWeight.Light,
											fontSize = 12.sp,
											modifier = Modifier
												.padding(4.dp)
										)
									}
									Spacer(Modifier.width(2.dp))
								}
							}
						}
						if (it.id == playerState.meta?.id && playlist.id == playerState.playlistId) {
							Icon(
								Icons.Filled.KeyboardArrowLeft,
								contentDescription = "current track cursor",
								modifier = Modifier
									.size(30.dp)
							)
						}
					}
				}
			}
			item {
				Spacer(Modifier.height(300.dp))
			}
		}
	}
}

@Composable
fun TopPlaylistCard(
	playlist: UserPlaylist.Entire,
	vm: PlaylistDetailsViewModel,
	playerState: PlayerState,
	onEnableTracksetEditing: ()	-> Unit,
) {
	Card(
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
		),
		modifier = Modifier
			.padding(12.dp)
			.fillMaxWidth()
			.height(132.dp)
	) {
		Row(
			modifier = Modifier
				.padding(6.dp)
		) {
			AsyncImage(
				model = playlist.imageUrl,
				contentDescription = "playlist image",
				modifier = Modifier
					.size(120.dp)
					.clip(
						RoundedCornerShape(8.dp)
					),
				contentScale = ContentScale.Crop,
			)
			Spacer(Modifier.width(18.dp))
			Column(
				modifier = Modifier
					.weight(1f)
					.fillMaxHeight(),
			) {
				Text(
					playlist.title,
					color = MaterialTheme.colorScheme.onSurface,
					fontFamily = getFont(),
					fontSize = 24.sp,
					fontWeight = FontWeight.ExtraBold,
				)
				Text(
					playlist.desc,
					color = MaterialTheme.colorScheme.onSurface,
					fontFamily = getFont(),
					fontSize = 20.sp,
					fontWeight = FontWeight.Normal
				)
				Spacer(modifier = Modifier.height(4.dp))
				Text(
					"${playlist.trackIds.size} Tracks",
					color = MaterialTheme.colorScheme.onSurface,
					fontFamily = getFont(),
					fontSize = 18.sp,
					fontWeight = FontWeight.Normal
				)
				Text(
					"${playlist.likes} Likes",
					color = MaterialTheme.colorScheme.onSurface,
					fontFamily = getFont(),
					fontSize = 18.sp,
					fontWeight = FontWeight.Normal
				)
				Spacer(Modifier.weight(1f))
			}
			Spacer(modifier = Modifier.width(12.dp))
			Box(
				modifier = Modifier.height(120.dp),
				contentAlignment = Alignment.Center
			) {
				IconButton(
					onClick = { vm.play() },
					modifier = Modifier.size(40.dp)
				) {
					Icon(
						painterResource(
							id = if (playerState.playing && playerState.playlistId == playlist.id) R.drawable.pause else R.drawable.play
						),
						contentDescription = "play from start"
					)
				}
			}
			Box(
				modifier = Modifier.height(120.dp),
				contentAlignment = Alignment.Center
			) {

				var expanded by rememberSaveable {
					mutableStateOf(false)
				}

				IconButton(
					onClick = { expanded = true},
					modifier = Modifier.size(40.dp)
				) {
					Icon(Icons.Filled.MoreVert, contentDescription = "details of playlist")
				}
				DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
					if (playlist.isEditable) {
						DropdownMenuItem(text = { Text("Edit track order") }, onClick = { onEnableTracksetEditing() })
					}

				}
			}
		}
	}
}

@Composable
fun Loading() {
	Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
		Card(
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
			),
		) {
			CircularProgressIndicator(
				modifier = Modifier.padding(12.dp)
			)
		}
	}
}
