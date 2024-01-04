package com.kaelesty.audionautica.presentation.navigation

sealed class Screen(
	val route: String
) {

	companion object {
		private const val ROUTE_MUSIC_SEARCH = "music_search"
		private const val ROUTE_MUSIC_PLAYLISTS = "music_playlists"
		private const val ROUTE_MUSIC_LIBRARY = "music_library"
	}

	object MusicSearch: Screen(ROUTE_MUSIC_SEARCH)
	object MusicPlaylists: Screen(ROUTE_MUSIC_PLAYLISTS)
	object MusicLibrary: Screen(ROUTE_MUSIC_LIBRARY)
}