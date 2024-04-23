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
	musicPlaylistsScreenContent: @Composable () -> Unit,
	musicLibraryScreenContent: @Composable () -> Unit,
	addPlaylistContent: @Composable () -> Unit,
	playlistDetailsContent: @Composable (UserPlaylist.Entire) -> Unit,
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
			composable(
				route = Screen.PlaylistsDetails.route,
				arguments = listOf(
					navArgument(Screen.KEY_PLAYLIST) {
						type = NavType.StringType
					}
				)
			) {
				val json = it.arguments?.getString(Screen.KEY_PLAYLIST)
				playlistDetailsContent(
					Gson().fromJson(json, UserPlaylist.Entire::class.java)
				)
			}
		}

		composable(Screen.MusicLibrary.route) {
			musicLibraryScreenContent()
		}
	}
}