package com.kaelesty.audionautica.presentation.composables.music

import android.media.session.MediaController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.composables.dialogues.AddTrackToPlalistDialog
import com.kaelesty.audionautica.presentation.composables.dialogues.EditPlaylistDialog
import com.kaelesty.audionautica.presentation.composables.dialogues.MinimalDialog
import com.kaelesty.audionautica.presentation.player.PlayerBar
import com.kaelesty.audionautica.presentation.navigation.MusicNavGraph
import com.kaelesty.audionautica.presentation.navigation.Screen
import com.kaelesty.audionautica.presentation.navigation.rememberMusicNavigationState
import com.kaelesty.audionautica.presentation.viewmodels.MusicViewModel
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MusicScreen(
	viewModelFactory: ViewModelFactory,
	offlineMode: Boolean,
	playerMediaControllerFlow: StateFlow<MediaController?>,
	launchCreateTrackActivity: () -> Unit,
) {

	val vm: MusicViewModel = viewModel(factory = viewModelFactory)
	val playerMediaController by playerMediaControllerFlow.collectAsState()

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

	val playlists by vm.getPlaylists().observeAsState(listOf())

	selectPlaylistDialog.value?.let { track ->
		AddTrackToPlalistDialog(
			onDimissRequest = { selectPlaylistDialog.value = null },
			onAcceptRequest = { playlistId ->
				vm.addTrackToPlaylist(track, playlistId)
				selectPlaylistDialog.value = null
			},
			playlists = playlists
		)
	}

	editPlaylistDialog.value?.let { playlist ->
		EditPlaylistDialog(
			onDismissRequest = { editPlaylistDialog.value = null },
			playlist = playlist,
			playlistTracks = vm.getPlaylistTracks(playlist.id),
			onDeleteTrackFromPlaylist = { track, id -> vm.removeTrackFromPlaylist(track, id) },
			onDeletePlaylist = { vm.deletePlaylist(it) }
		)
	}

	createPlaylistDialog.value?.let {
		MinimalDialog(
			onAcceptRequest = {
				createPlaylistDialog.value = null
				vm.createPlaylist(
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
				playerMediaController?.let {
					PlayerBar(
						playerMediaController = it,
						viewModelFactory = viewModelFactory
					)
				}
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
					onSearch = { vm.search(it) },
					tracksSearchResults = vm.tracksSearchResults,
					onPlay = { vm.playTrack(it) },
					selectPlaylistDialog = selectPlaylistDialog,
					onSaveTrack = { vm.saveTrack(it) },
					libraryTracksLD = vm.getAllTracks(),
					playerMediaController = playerMediaController
				)
			},
			musicPlaylistsScreenContent = {
				Playlists(
					playlistsLiveData = vm.getPlaylists(),
					editPlaylistDialog = editPlaylistDialog,
					onPlayPlaylist = { vm.playPlaylist(it) },
					createPlaylistDialog = createPlaylistDialog
				)
			},
			musicLibraryScreenContent = {
				MyTracks(
					onCreateTrack = { launchCreateTrackActivity() },
					libraryTracks = vm.getAllTracks(),
					onDeleteTrack = { vm.deleteTrack(it) },
					onPlay = { vm.playTrack(it) },
					selectPlaylistDialog = selectPlaylistDialog,
					offlineMode = offlineMode,
					playerMediaController = playerMediaController
				)
			},
		)
	}
}

