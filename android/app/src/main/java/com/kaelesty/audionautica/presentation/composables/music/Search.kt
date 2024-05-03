package com.kaelesty.audionautica.presentation.composables.music

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.playlists.addtracktoplaylist.AddTrackToPlalistDialog
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Search(
	onSearch: (String) -> Unit,
	tracksSearchResults: LiveData<List<Track>>,
	onPlay: (Track) -> Unit,
	onSaveTrack: (Track) -> Unit,
	libraryTracksLD: LiveData<List<Track>>,
	viewModelFactory: ViewModelFactory
) {
	val tracks by tracksSearchResults.observeAsState(listOf())


	val libraryTracks by libraryTracksLD.observeAsState(initial = listOf())

	var selectPlaylistDialog by rememberSaveable { mutableStateOf<Track?>(null) }

	selectPlaylistDialog?.let { track ->
		AddTrackToPlalistDialog(
			onDismissRequest = { selectPlaylistDialog = null },
			track = track,
			viewmodelFactory = viewModelFactory
		)
	}

	LazyColumn(
		modifier = Modifier
			.fillMaxSize(),
		content = {
			stickyHeader { MusicSearchBar(onSearch) }
			items(tracks, key = { it.id }) {
				TrackCard(
					track = it,
					onClick = { clickedTrack ->
						onPlay(it)
					},
					onAdd = {
						selectPlaylistDialog = it
					},
					onSecondary = if (it !in libraryTracks) {
						{ onSaveTrack(it) }
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