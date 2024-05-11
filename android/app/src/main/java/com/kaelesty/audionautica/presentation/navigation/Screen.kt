package com.kaelesty.audionautica.presentation.navigation

import android.net.Uri
import com.google.gson.Gson
import com.kaelesty.audionautica.domain.playlists.UserPlaylist

sealed class Screen(
	val route: String
) {

	companion object {

		const val KEY_PLAYLIST = "playlist"
		const val KEY_LIST_SOURCE = "source"

		private const val ROUTE_MUSIC_SEARCH = "music_search"
		private const val ROUTE_PLAYLISTS_ORIGIN = "playlists"

		private const val ROUTE_PLAYLISTS_LIST = "playlists_list/{$KEY_LIST_SOURCE}"
		private const val ROUTE_PLAYLISTS_CREATE = "playlists_create"
		private const val ROUTE_PLAYLISTS_DETAILS = "playlists_details/{$KEY_PLAYLIST}"

		private const val ROUTE_MUSIC_LIBRARY = "music_library"

		private const val ROUTE_SETTINGS = "settings"
	}

	object MusicSearch: Screen(ROUTE_MUSIC_SEARCH)

	object Playlists: Screen(ROUTE_PLAYLISTS_ORIGIN)
	object PlaylistsList: Screen(ROUTE_PLAYLISTS_LIST) {

		enum class PlaylistsSource(val str: String) {
			Remote("remote"), Local("local");

			companion object {
				fun fromString(string: String) = when(string) {
					Remote.str -> Remote
					Local.str -> Local
					else -> throw IllegalArgumentException("Could not convert $string to PlaylistsSource")
				}
			}
		}

		private const val ROUTE = "playlists_list"

		fun getRouteWithArgs(source: PlaylistsSource) = "$ROUTE/${source.str}"
	}


	object PlaylistsCreate: Screen(ROUTE_PLAYLISTS_CREATE)

	object PlaylistsDetails: Screen(ROUTE_PLAYLISTS_DETAILS) {

		private const val ROUTE = "playlists_details"

		fun getRouteWithArgs(playlist: UserPlaylist.Entire): String {
			return "$ROUTE/${Gson().toJson(playlist).encode()}"
		}
	}

	object MusicLibrary: Screen(ROUTE_MUSIC_LIBRARY)

	object Settings: Screen(ROUTE_SETTINGS)
}

fun String.encode() = Uri.encode(this)