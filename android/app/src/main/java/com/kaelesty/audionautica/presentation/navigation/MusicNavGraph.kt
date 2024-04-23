package com.kaelesty.audionautica.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

@Composable
fun MusicNavGraph(
	navHostController: NavHostController,
	startDestination: String,
	musicSearchScreenContent: @Composable () -> Unit,
	musicPlaylistsScreenContent: @Composable () -> Unit,
	musicLibraryScreenContent: @Composable () -> Unit,
	addPlaylistContent: @Composable () -> Unit,
) {
	NavHost(
		navController = navHostController,
		startDestination = startDestination,
	) {

		composable(Screen.MusicSearch.route) {
			musicSearchScreenContent()
		}

		navigation(
			startDestination = Screen.PlaylistsList.route,
			route = Screen.Playlists.route
		) {
			composable(Screen.PlaylistsList.route) {
				musicPlaylistsScreenContent()
			}
			composable(Screen.PlaylistsCreate.route) {
				addPlaylistContent()
			}
		}

		composable(Screen.MusicLibrary.route) {
			musicLibraryScreenContent()
		}
	}
}