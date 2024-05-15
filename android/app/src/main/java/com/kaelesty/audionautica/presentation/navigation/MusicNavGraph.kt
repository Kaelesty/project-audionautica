package com.kaelesty.audionautica.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.gson.Gson
import com.kaelesty.audionautica.domain.playlists.UserPlaylist

@Composable
fun MusicNavGraph(
	navHostController: NavHostController,
	startDestination: String,
	musicSearchScreenContent: @Composable () -> Unit,
	musicPlaylistsScreenContent: @Composable (Screen.PlaylistsList.PlaylistsSource) -> Unit,
	musicLibraryScreenContent: @Composable () -> Unit,
	addPlaylistContent: @Composable () -> Unit,
	playlistDetailsContent: @Composable (Int) -> Unit,
	settingsContent: @Composable () -> Unit,
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
			composable(
				route = Screen.PlaylistsList.route,
				arguments = listOf(
					navArgument(Screen.KEY_LIST_SOURCE) {
						type = NavType.StringType
					}
				)
			) {
				val str = it.arguments?.getString(Screen.KEY_LIST_SOURCE) ?: Screen.PlaylistsList.PlaylistsSource.Remote.str
				musicPlaylistsScreenContent(
					Screen.PlaylistsList.PlaylistsSource.fromString(str)
				)
			}
			composable(Screen.PlaylistsCreate.route) {
				addPlaylistContent()
			}
			composable(
				route = Screen.PlaylistsDetails.route,
				arguments = listOf(
					navArgument(Screen.KEY_PLAYLIST_ID) {
						type = NavType.IntType
					}
				)
			) {
				val id = it.arguments?.getInt(Screen.KEY_PLAYLIST_ID) ?: -1
				playlistDetailsContent(
					id
				)
			}
		}

		composable(Screen.MusicLibrary.route) {
			musicLibraryScreenContent()
		}

		composable(Screen.Settings.route) {
			settingsContent()
		}
	}
}