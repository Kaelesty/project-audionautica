package com.kaelesty.audionautica.presentation.playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.audionautica.domain.playlists.Playlist
import com.kaelesty.audionautica.domain.playlists.UserPlaylists
import com.kaelesty.audionautica.presentation.composables.dialogues.LoadingCard
import com.kaelesty.audionautica.presentation.composables.dialogues.LoadingDialog
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory

@Composable
fun Playlists(
	viewModelFactory: ViewModelFactory,
	navigateToAddScreen: () -> Unit,
) {
	val vm = viewModelFactory.create(PlaylistsViewModel::class.java)
	vm.loadPlaylists()
	val userPlaylists by vm.stateFlow.collectAsState()

	when (userPlaylists) {
		is PlaylistsState.Loading -> {
			LoadingDialog()
		}

		is PlaylistsState.Empty -> {
			Column(
				modifier = Modifier
					.padding(12.dp)
					.fillMaxHeight(),
			) {}
			AddButton(
				action = { navigateToAddScreen() }
			)
		}

		is PlaylistsState.Result -> {
			val cards = (userPlaylists as PlaylistsState.Result).cards
			LazyColumn(
				modifier = Modifier
					.padding(12.dp)
					.fillMaxHeight(),
				content = {
					items(
						items = cards,
						key = {
							when (it) {
								is PlaylistCardState.IdOnly -> {
									it.id
								}
								is PlaylistCardState.Full -> {
									it.playlist.id
								}
							}
						}
					) {
						PlaylistCard(state = it)
					}
					item {
						Spacer(Modifier.height(300.dp))
					}
				}
			)
			AddButton(
				action = { navigateToAddScreen() }
			)
		}
	}
}

@Composable
fun AddButton(
	action: () -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxSize()
			.padding(bottom = 242.dp, end = 12.dp)
		// 192.dp to make position equals with button of MyTracks
		,
		verticalAlignment = Alignment.Bottom
	) {
		Spacer(Modifier.weight(1f))
		IconButton(
			onClick = {
				action
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