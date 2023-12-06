package com.kaelesty.audionautica.presentation.composables.music

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.composables.dialogues.AddTrackToPlalistDialog
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.composables.dialogues.EditPlaylistDialog
import com.kaelesty.audionautica.presentation.composables.dialogues.MinimalDialog
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

//@Preview
//@Composable
//fun MusicScreenPreview() {
//	AudionauticaTheme {
//		MusicScreen(
//			onPlay = {},
//			onPause = { },
//			onResume = { },
//			onSearch = {},
//			onAddTrackToPlaylist = { track, id -> },
//			onRemoveTrackFromPlaylist = { track, id -> },
//			tracksSearchResults = MutableLiveData(),
//			playingFlow = MutableSharedFlow(),
//			trackFlow = MutableSharedFlow(),
//			onRequestTrackCreation = {},
//			playlistsLiveData = MutableLiveData(),
//			getPlaylistTracks = { MutableLiveData() },
//		)
//	}
//}

@Composable
fun MusicScreen(
	onPlay: (Track) -> Unit,
	onPause: () -> Unit,
	onResume: () -> Unit,
	onSearch: (String) -> Unit,
	onAddTrackToPlaylist: (Track, Int) -> Unit,
	onRemoveTrackFromPlaylist: (Track, Int) -> Unit,
	tracksSearchResults: LiveData<List<Track>>,
	playingFlow: SharedFlow<Boolean>,
	trackFlow: SharedFlow<Track>,
	onRequestTrackCreation: () -> Unit,
	playlistsLiveData: LiveData<List<Playlist>>,
	getPlaylistTracks: (Int) -> List<Track>,
	onDeleteTrackFromPlaylist: (Track, Int) -> Unit,
	onCreatePlaylist: (Playlist) -> Unit,
	onDeletePlaylist: (Int) -> Unit,
	onPlayPlaylist: (Int) -> Unit
) {

	val mode = rememberSaveable {
		mutableStateOf(MusicScreenMode.SEARCH)
	}

	val systemUiController = rememberSystemUiController()
	systemUiController.setSystemBarsColor(
		color = Color.White
	)

	val selectPlaylistDialog = rememberSaveable {
		mutableStateOf<Track?>(null)
	}

	val editPlaylistDialog = rememberSaveable {
		mutableStateOf<Playlist?>(null)
	}

	val createPlaylistDialog = rememberSaveable {
		mutableStateOf<Unit?>(null)
	}

	val playlists by playlistsLiveData.observeAsState(listOf())

	selectPlaylistDialog.value?.let { track ->
		AddTrackToPlalistDialog(
			onDimissRequest = { selectPlaylistDialog.value = null },
			onAcceptRequest = { playlistId ->
				onAddTrackToPlaylist(
					track, playlistId
				)
				selectPlaylistDialog.value = null
			},
			playlists = playlists
		)
	}

	editPlaylistDialog.value?.let { playlist ->
		EditPlaylistDialog(
			onDismissRequest = { editPlaylistDialog.value = null },
			playlist = playlist,
			playlistTracks = getPlaylistTracks(playlist.id),
			onDeleteTrackFromPlaylist = onDeleteTrackFromPlaylist,
			onDeletePlaylist = onDeletePlaylist
		)
	}

	createPlaylistDialog.value?.let {
		MinimalDialog(
			onAcceptRequest = {
				createPlaylistDialog.value = null
				onCreatePlaylist(
					Playlist(
						id = -1,
						title = it,
						trackIds = listOf()
					)
				)
			},
			onDismissRequest = {
				createPlaylistDialog.value = null
			},
			title = "Enter playlist name"
		)
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize(),
		bottomBar = {
			Column {
				PlayerBar(
					onResume = { onResume() },
					onPause = { onPause() },
					playingFlow = playingFlow,
					trackFlow = trackFlow
				)
				MusicNavigationBar(mode)
			}
		},
	) {
		it
		Image(
			painter = painterResource(
				id = R.drawable.space_background
			),
			contentDescription = "Space Background",
			modifier = Modifier
				.fillMaxSize(),
			contentScale = ContentScale.Crop
		)
		when (mode.value) {
			MusicScreenMode.SEARCH -> Search(
				onSearch = onSearch,
				tracksSearchResults = tracksSearchResults,
				onPlay = onPlay,
				onPause = onPause,
				onAddTrackToPlaylist = onAddTrackToPlaylist,
				playingFlow = playingFlow,
				selectPlaylistDialog = selectPlaylistDialog
			)

			MusicScreenMode.PLAYLISTS -> Playlists(
				playlistsLiveData = playlistsLiveData,
				editPlaylistDialog = editPlaylistDialog,
				onPlayPlaylist = onPlayPlaylist,
				onDeleteTrackFromPlaylist = onRemoveTrackFromPlaylist,
				createPlaylistDialog = createPlaylistDialog
			)

			MusicScreenMode.MY_TRACKS -> MyTracks(
				onCreateTrack = { onRequestTrackCreation() }
			)
		}
	}
}

//@Preview
//@Composable
//fun PlaylistsPreview() {
//	AudionauticaTheme {
//		Playlists(playlistsLiveData = MutableLiveData(
//			listOf(
//				Playlist(0, "Favorites", listOf()),
//				Playlist(1, "Playlist 1", listOf()),
//				Playlist(2, "Playlist 2", listOf()),
//			)
//		))
//	}
//}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Playlists(
	playlistsLiveData: LiveData<List<Playlist>>,
	onPlayPlaylist: (Int) -> Unit = {},
	onDeleteTrackFromPlaylist: (Track, Int) -> Unit = { track, id -> },
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
			.padding(bottom = 172.dp, end = 12.dp)
		// 172.dp to make position equals with button of MyTracks
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

@Composable
fun MyTracks(
	onCreateTrack: () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(12.dp),
		horizontalAlignment = Alignment.End
	) {
		Spacer(Modifier.weight(1f))
		IconButton(
			onClick = { onCreateTrack() },
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
		Spacer(Modifier.height(160.dp))
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Search(
	onSearch: (String) -> Unit,
	tracksSearchResults: LiveData<List<Track>>,
	onPlay: (Track) -> Unit,
	onPause: () -> Unit,
	onAddTrackToPlaylist: (Track, Int) -> Unit,
	playingFlow: SharedFlow<Boolean>,
	selectPlaylistDialog: MutableState<Track?>
) {
	val tracks by tracksSearchResults.observeAsState(listOf())
	val playing by playingFlow.collectAsState(initial = false)

	LazyColumn(
		modifier = Modifier
			.fillMaxSize(),
		content = {
			stickyHeader { MusicSearchBar(onSearch) }
			items(tracks, key = { it.id }) {
				TrackSearchResult(
					track = it,
					onClick = { track ->
						if (playing) {
							onPause()
						} else {
							onPlay(it)
						}
					},
					onAdd = {
						selectPlaylistDialog.value = it
					}
				)
			}
			item {
				Spacer(modifier = Modifier.height(300.dp))
			}
		},

		)
}

@Composable
fun PlayerBar(
	onResume: () -> Unit,
	onPause: () -> Unit,
	playingFlow: SharedFlow<Boolean>,
	trackFlow: SharedFlow<Track>
) {

	val playing by playingFlow.collectAsState(initial = false)
	val track by trackFlow.collectAsState(initial = Track(- 1, "", "", listOf()))
	GradientCard {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 6.dp, vertical = 4.dp)
		) {
			Text(
				text = track.title + " - " + track.artist,
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.Normal,
				fontSize = 16.sp,
				modifier = Modifier
					.weight(1f)
			)
			Icon(
				imageVector = if (! playing) Icons.Outlined.PlayArrow else Icons.Outlined.Clear,
				contentDescription = null,
				modifier = Modifier
					.clickable {
						if (playing) {
							onPause()
						} else {
							onResume()
						}
					}
			)
		}
	}
}

@Composable
fun MusicSearchBar(
	onSearch: (String) -> Unit,
) {

	var query by rememberSaveable {
		mutableStateOf("")
	}

	GradientCard {
		OutlinedTextField(
			value = query,
			onValueChange = {
				query = it
				onSearch(it)
			},
			leadingIcon = {
				Icon(
					Icons.Outlined.Search,
					contentDescription = "Search Icon",
					modifier = Modifier
						.padding(8.dp)

				)
			},
			modifier = Modifier.fillMaxWidth(),
			singleLine = true
		)
	}
}

@Composable
fun MusicNavigationBar(mode: MutableState<MusicScreenMode>) {
	var selectedItem by rememberSaveable {
		mutableStateOf(0)
	}
	val items = listOf("Search", "Playlists", "My Tracks")

	NavigationBar(
		containerColor = Color.Transparent,
		modifier = Modifier.height(100.dp)
	) {
		GradientCard {
			Row(verticalAlignment = Alignment.CenterVertically) {
				items.forEachIndexed { index, item ->
					NavigationBarItem(
						selected = selectedItem == index,
						onClick = {
							selectedItem = index
							when (index) {
								0 -> {
									mode.value = MusicScreenMode.SEARCH
								}

								1 -> {
									mode.value = MusicScreenMode.PLAYLISTS
								}

								2 -> {
									mode.value = MusicScreenMode.MY_TRACKS
								}
							}
						},
						icon = {
							Icon(
								when (item) {
									"Search" -> Icons.Outlined.Search
									"Playlists" -> Icons.Outlined.List
									"My Tracks" -> Icons.Outlined.FavoriteBorder
									else -> Icons.Outlined.Warning
								},
								"Search Icon"
							)
						},
						modifier = Modifier.height(80.dp)
					)
				}
			}
		}
	}
}