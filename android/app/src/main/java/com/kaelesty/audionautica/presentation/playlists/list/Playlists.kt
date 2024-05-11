package com.kaelesty.audionautica.presentation.playlists.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.presentation.navigation.Screen
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory

@Composable
fun Playlists(
	viewModelFactory: ViewModelFactory,
	navigateToAddScreen: () -> Unit,
	navigateToDetailsScreen: (UserPlaylist.Entire) -> Unit,
	navigateToLocalList: () -> Unit,
	navigateToRemoteList: () -> Unit,
	source: Screen.PlaylistsList.PlaylistsSource,
) {

	if (source == Screen.PlaylistsList.PlaylistsSource.Local) {
		Text("Local! not yet implemented:)")
		DownsideButton(
			action = {
				navigateToRemoteList()
			},
			icon = Icons.Default.ExitToApp,
			level = 0
		)
		return
	}
	
	val vm = viewModelFactory.create(PlaylistsViewModel::class.java)
	Log.d("ReCoTag", "Playlists")


	val playlists by vm.playlistCollection.observeAsState(initial = listOf())
	if (playlists.isEmpty()) {
		SideEffect {
			vm.updatePlaylistCollection()
		}
	}

	LazyColumn(
		modifier = Modifier
			.padding(12.dp)
			.fillMaxHeight(),
		content = {
			items(
				items = playlists,
				key = {
					it.id
				}
			) {
				PlaylistCard(
					state = it,
					onShowPlaylistDetails = {
						navigateToDetailsScreen(it)
					}
				)
				Spacer(modifier = Modifier.height(12.dp))
			}
			item {
				Spacer(Modifier.height(300.dp))
			}
		}
	)
	DownsideButton(
		action = {
			navigateToAddScreen()
		},
		icon = Icons.Default.Add,
		level = 1
	)
	DownsideButton(
		action = {
			navigateToLocalList()
		},
		icon = Icons.Default.FavoriteBorder,
		level = 0
	)
}

@Composable
fun DownsideButton(
	action: () -> Unit,
	level: Int = 0,
	icon: ImageVector
) {
	Row(
		modifier = Modifier
			.fillMaxSize()
			.padding(bottom = 242.dp + 60.dp * level, end = 12.dp)
		,
		verticalAlignment = Alignment.Bottom
	) {
		Spacer(Modifier.weight(1f))
		IconButton(
			onClick = {
				action()
			},
			modifier = Modifier
				.background(
					color = MaterialTheme.colorScheme.surface,
					shape = RoundedCornerShape(4.dp)
				)

		) {
			Icon(
				imageVector = icon,
				contentDescription = null
			)
		}
	}
}