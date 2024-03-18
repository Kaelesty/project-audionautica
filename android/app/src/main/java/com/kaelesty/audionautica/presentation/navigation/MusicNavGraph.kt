package com.kaelesty.audionautica.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MusicNavGraph(
	navHostController: NavHostController,
	startDestination: String,
	musicSearchScreenContent: @Composable () -> Unit,
	musicPlaylistsScreenContent: @Composable () -> Unit,
	musicLibraryScreenContent: @Composable () -> Unit,
) {
	NavHost(
		navController = navHostController,
		startDestination = startDestination,
	) {

		composable(Screen.MusicSearch.route) {
			musicSearchScreenContent()
		}

		composable(Screen.MusicPlaylists.route) {
			musicPlaylistsScreenContent()
		}

		composable(Screen.MusicLibrary.route) {
			musicLibraryScreenContent()
		}
	}
}