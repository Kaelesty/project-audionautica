package com.kaelesty.audionautica.presentation.composables.music

import android.R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Track
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Search(
	onSearch: (String) -> Unit,
	tracksSearchResults: LiveData<List<Track>>,
	onPlay: (Track) -> Unit,
	onPause: () -> Unit,
	playingFlow: SharedFlow<Boolean>,
	selectPlaylistDialog: MutableState<Track?>,
	onSaveTrack: (Track) -> Unit,
	libraryTracksLD: LiveData<List<Track>>,
	trackFlow: SharedFlow<Track>,
	pausedTrackId: MutableState<Int>,
	onResume: () -> Unit,
) {
	val tracks by tracksSearchResults.observeAsState(listOf())
	val playing by playingFlow.collectAsState(initial = false)

	val libraryTracks by libraryTracksLD.observeAsState(initial = listOf())

	val track by trackFlow.collectAsState(initial = Track(- 1, "", "", listOf()))

	LazyColumn(
		modifier = Modifier
			.fillMaxSize(),
		content = {
			stickyHeader { MusicSearchBar(onSearch) }
			items(tracks, key = { it.id }) {
				TrackCard(
					track = it,
					onClick = { clickedTrack ->
						if (playing) {
							if (track.id == clickedTrack.id) {
								onPause()
								pausedTrackId.value = track.id
							} else {
								onPlay(clickedTrack)
							}
						} else {
							if (pausedTrackId.value == clickedTrack.id) {
								onResume()
							} else {
								onPlay(clickedTrack)
							}
						}
					},
					onAdd = {
						selectPlaylistDialog.value = it
					},
					onSecondary = if (it !in libraryTracks) {
						{ onSaveTrack(it) }
					} else {
						null
					},
					secondaryIcon = R.drawable.ic_menu_save
				)
			}
			item {
				Spacer(modifier = Modifier.height(300.dp))
			}
		},

		)
}