package com.kaelesty.audionautica.presentation.navigation

sealed class Screen(
	val route: String
) {

	companion object {
		private const val ROUTE_MUSIC_SEARCH = "music_search"
		private const val ROUTE_PLAYLISTS_ORIGIN = "playlists"
		private const val ROUTE_PLAYLISTS_LIST = "playlists_list"
		private const val ROUTE_PLAYLISTS_CREATE = "playlists_create"

		private const val ROUTE_MUSIC_LIBRARY = "music_library"
	}

	object MusicSearch: Screen(ROUTE_MUSIC_SEARCH)

	object Playlists: Screen(ROUTE_PLAYLISTS_ORIGIN)
	object PlaylistsList: Screen(ROUTE_PLAYLISTS_LIST)
	object PlaylistsCreate: Screen(ROUTE_PLAYLISTS_CREATE)

	object MusicLibrary: Screen(ROUTE_MUSIC_LIBRARY)
}