package com.kaelesty.audionautica.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.presentation.composables.music.TrackCard
import com.kaelesty.audionautica.presentation.playlists.addtracktoplaylist.AddTrackToPlalistDialog
import com.kaelesty.audionautica.presentation.playlists.details.Loading
import com.kaelesty.audionautica.presentation.playlists.list.PlaylistCard
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory

@Composable
fun Search(
	viewModelFactory: ViewModelFactory,
	onNavigateToPlaylistDetails: (UserPlaylist.Entire) -> Unit
) {

	val vm = viewModel<SearchViewModel>(factory = viewModelFactory)

	val state by vm.stateFlow.collectAsState()

	Column {
		MusicSearchBar(vm)
		when (state) {
			is SearchState.Initial -> {

			}

			is SearchState.Loading -> {
				Loading()
			}

			is SearchState.TracksResult -> {
				TracksList(vm, (state as SearchState.TracksResult).tracks, viewModelFactory)
			}

			is SearchState.PlaylistsResult -> {
				PlaylistsList(
					vm,
					(state as SearchState.PlaylistsResult).playlists,
					onNavigateToPlaylistDetails
				)
			}
		}
	}
}

@Composable
fun TracksList(
	vm: SearchViewModel,
	tracks: List<Track>,
	viewModelFactory: ViewModelFactory
) {
	var selectPlaylistDialog by rememberSaveable { mutableStateOf<Track?>(null) }
	selectPlaylistDialog?.let { track ->
		AddTrackToPlalistDialog(
			onDismissRequest = { selectPlaylistDialog = null },
			track = track,
			viewmodelFactory = viewModelFactory
		)
	}
	val tracksLibrary by vm.tracksLibrary.observeAsState(listOf())

	LazyColumn(
		modifier = Modifier
			.fillMaxSize(),
		content = {
			items(tracks) {
				TrackCard(
					track = it,
					onClick = { clickedTrack ->
						vm.playTrack(clickedTrack)
					},
					onAdd = {
						selectPlaylistDialog = it
					},
					onSecondary = if (it !in tracksLibrary) {
						{ vm.saveTrackToLibrary(it) }
					} else {
						null
					},
					secondaryIcon = android.R.drawable.ic_menu_save
				)
			}
			item {
				Spacer(modifier = Modifier.height(300.dp))
			}
		},

		)
}

@Composable
fun PlaylistsList(
	vm: SearchViewModel,
	playlists: List<UserPlaylist.Entire>,
	onNavigateToPlaylistDetails: (UserPlaylist.Entire) -> Unit
) {
	playlists.forEach {
		Box(
			modifier = Modifier
				.height(144.dp)
				.padding(horizontal = 8.dp)
				.padding(bottom = 10.dp)
		) {
			PlaylistCard(state = it, onShowPlaylistDetails = {
				onNavigateToPlaylistDetails(it)
			})
		}
	}
}