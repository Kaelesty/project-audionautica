package com.kaelesty.audionautica.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
	val screen: Screen,
	val icon: ImageVector,
	val description: String
) {

	object MusicSearch: NavigationItem(
		Screen.MusicSearch,
		icon = Icons.Outlined.Search,
		description = "Search Navigation Icon",
	)

	object MusicPlaylists: NavigationItem(
		Screen.MusicPlaylists,
		icon = Icons.Outlined.List,
		description = "Playlists Navigation Icon",
	)

	object MusicLibrary: NavigationItem(
		Screen.MusicLibrary,
		icon = Icons.Outlined.FavoriteBorder,
		description = "Library Navigation Icon",
	)
}