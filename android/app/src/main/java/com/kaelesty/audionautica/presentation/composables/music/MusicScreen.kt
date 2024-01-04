package com.kaelesty.audionautica.presentation.composables.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.LiveData
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.composables.dialogues.AddTrackToPlalistDialog
import com.kaelesty.audionautica.presentation.composables.dialogues.EditPlaylistDialog
import com.kaelesty.audionautica.presentation.composables.dialogues.MinimalDialog
import com.kaelesty.audionautica.presentation.navigation.MusicNavGraph
import com.kaelesty.audionautica.presentation.navigation.Screen
import com.kaelesty.audionautica.presentation.navigation.rememberMusicNavigationState
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun MusicScreen(
	onPlay: (Track) -> Unit,
	onPause: () -> Unit,
	onResume: () -> Unit,
	onNext: () -> Unit,
	onPrev: () -> Unit,
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
	onPlayPlaylist: (Int) -> Unit,
	libraryTracks: LiveData<List<Track>>,
	onSaveTrack: (Track) -> Unit,
	onDeleteTrack: (Track) -> Unit,
	offlineMode: Boolean
) {

	var pausedTrackId = rememberSaveable {
		mutableStateOf(- 1)
	}

	val navigationState = rememberMusicNavigationState()

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
						id = - 1,
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
					trackFlow = trackFlow,
					pausedTrackId = pausedTrackId,
					onNext = onNext,
					onPrev = onPrev,
				)
				MusicNavigationBar(navigationState, offlineMode)
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

		MusicNavGraph(
			navHostController = navigationState.navHostController,
			startDestination =
			if (! offlineMode) Screen.MusicSearch.route
			else Screen.MusicLibrary.route,
			musicSearchScreenContent = {
				Search(
					onSearch = onSearch,
					tracksSearchResults = tracksSearchResults,
					onPlay = onPlay,
					onPause = onPause,
					playingFlow = playingFlow,
					selectPlaylistDialog = selectPlaylistDialog,
					onSaveTrack = onSaveTrack,
					libraryTracksLD = libraryTracks,
					trackFlow = trackFlow,
					pausedTrackId = pausedTrackId,
					onResume = onResume
				)
			},
			musicPlaylistsScreenContent = {
				Playlists(
					playlistsLiveData = playlistsLiveData,
					editPlaylistDialog = editPlaylistDialog,
					onPlayPlaylist = onPlayPlaylist,
					createPlaylistDialog = createPlaylistDialog
				)
			},
			musicLibraryScreenContent = {
				MyTracks(
					onCreateTrack = { onRequestTrackCreation() },
					libraryTracks = libraryTracks,
					onDeleteTrack = onDeleteTrack,
					onPause = onPause,
					onPlay = onPlay,
					playingFlow = playingFlow,
					selectPlaylistDialog = selectPlaylistDialog,
					trackFlow = trackFlow,
					pausedTrackId = pausedTrackId,
					onResume = onResume,
					offlineMode = offlineMode
				)
			},
		)
	}
}

