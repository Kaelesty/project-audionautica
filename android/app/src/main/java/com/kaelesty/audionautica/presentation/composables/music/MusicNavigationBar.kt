package com.kaelesty.audionautica.presentation.composables.music

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.navigation.MusicNavigationState
import com.kaelesty.audionautica.presentation.navigation.NavigationItem
import com.kaelesty.audionautica.presentation.navigation.Screen

@Composable
fun MusicNavigationBar(
	navigationState: MusicNavigationState,
	offlineMode: Boolean
) {

	val items = if (! offlineMode) {
		listOf(
			NavigationItem.MusicSearch,
			NavigationItem.MusicPlaylists,
			NavigationItem.MusicLibrary,
		)
	} else {
		listOf(
			NavigationItem.MusicPlaylists,
			NavigationItem.MusicLibrary,
		)
	}

	val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

	NavigationBar(
		containerColor = Color.Transparent,
		modifier = Modifier.height(100.dp)
	) {
		GradientCard {
			Row(verticalAlignment = Alignment.CenterVertically) {
				items.forEach { item ->
					NavigationBarItem(
						selected = navBackStackEntry?.destination?.route == item.screen.route,
						onClick = {
							navigationState.navigateTo(item.screen.route)
						},
						icon = {
							Icon(
								item.icon,
								contentDescription = item.description,
							)
						},
						modifier = Modifier.height(80.dp)
					)
				}
			}
		}
	}
}