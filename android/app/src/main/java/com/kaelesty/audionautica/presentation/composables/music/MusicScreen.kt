package com.kaelesty.audionautica.presentation.composables.music

import android.media.session.MediaController
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.presentation.player.PlayerBar
import com.kaelesty.audionautica.presentation.navigation.MusicNavGraph
import com.kaelesty.audionautica.presentation.navigation.Screen
import com.kaelesty.audionautica.presentation.navigation.rememberMusicNavigationState
import com.kaelesty.audionautica.presentation.playlists.details.PlaylistDetails
import com.kaelesty.audionautica.presentation.playlists.upload.AddPlaylistScreen
import com.kaelesty.audionautica.presentation.playlists.list.Playlists
import com.kaelesty.audionautica.presentation.viewmodels.MusicViewModel
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MusicScreen(
	viewModelFactory: ViewModelFactory,
	offlineMode: Boolean,
	launchCreateTrackActivity: () -> Unit,

	onRequestImage: () -> Unit,
	requestedImageFlow: SharedFlow<Uri?>,
) {

	val vm: MusicViewModel = viewModel(factory = viewModelFactory)

	val navigationState = rememberMusicNavigationState()

	val systemUiController = rememberSystemUiController()
	systemUiController.setSystemBarsColor(
		color = Color.White
	)




	Scaffold(
		modifier = Modifier
			.fillMaxSize(),
		bottomBar = {
			Column {
				PlayerBar(
					viewModelFactory = viewModelFactory
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
			if (! offlineMode) Screen.Playlists.route
			else Screen.MusicLibrary.route,
			musicSearchScreenContent = {
				Search(
					onSearch = { vm.search(it) },
					tracksSearchResults = vm.tracksSearchResults,
					onPlay = { vm.playTrack(it) },
					onSaveTrack = { vm.saveTrack(it) },
					libraryTracksLD = vm.getAllTracks(),
				)
			},
			musicPlaylistsScreenContent = {
				Playlists(
					viewModelFactory,
					navigateToAddScreen = {
						Log.d("NavTag", "to AddScreen")
						navigationState.navHostController.navigate(Screen.PlaylistsCreate.route)
					},
					navigateToDetailsScreen = {
						navigationState.navHostController.navigate(Screen.PlaylistsDetails.getRouteWithArgs(it))
					}
				)
			},
			musicLibraryScreenContent = {
				MyTracks(
					onCreateTrack = { launchCreateTrackActivity() },
					libraryTracks = vm.getAllTracks(),
					onDeleteTrack = { vm.deleteTrack(it) },
					onPlay = { vm.playTrack(it) },
					//selectPlaylistDialog = selectPlaylistDialog,
					offlineMode = offlineMode,
				)
			},
			addPlaylistContent = {
				AddPlaylistScreen(
					viewModelFactory,
					onRequestImage,
					requestedImageFlow,
					navigateToListScreen = {
						Log.d("NavTag", "pop backstack")
						navigationState.navHostController.popBackStack()
					}
				)
			},
			playlistDetailsContent = {
				PlaylistDetails(playlist = it, viewModelFactory = viewModelFactory)
			}
		)
	}
}

