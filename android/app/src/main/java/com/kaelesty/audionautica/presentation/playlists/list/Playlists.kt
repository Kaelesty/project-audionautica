package com.kaelesty.audionautica.presentation.playlists.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.audionautica.data.playlists.UserPlaylistResponse
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.presentation.composables.dialogues.LoadingDialog
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory

@Composable
fun Playlists(
	viewModelFactory: ViewModelFactory,
	navigateToAddScreen: () -> Unit,
	navigateToDetailsScreen: (UserPlaylist.Entire) -> Unit,
) {
	val vm = viewModelFactory.create(PlaylistsViewModel::class.java)
	Log.d("ReCoTag", "Playlists")

	//vm.loadPlaylists()

	val playlists by vm.playlistCollection.observeAsState(initial = listOf())

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
	AddButton(
		action = {
			navigateToAddScreen()
		}
	)
}

@Composable
fun AddButton(
	action: () -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxSize()
			.padding(bottom = 242.dp, end = 12.dp)
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
				imageVector = Icons.Filled.Add,
				contentDescription = null
			)
		}
	}
}