package com.kaelesty.audionautica.presentation.composables.music

import android.media.session.MediaController
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.player.getCurrentTrack
import com.kaelesty.audionautica.presentation.player.isPlaying

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Search(
	onSearch: (String) -> Unit,
	tracksSearchResults: LiveData<List<Track>>,
	onPlay: (Track) -> Unit,
	playerMediaController: MediaController?,
	selectPlaylistDialog: MutableState<Track?>,
	onSaveTrack: (Track) -> Unit,
	libraryTracksLD: LiveData<List<Track>>,
) {
	val tracks by tracksSearchResults.observeAsState(listOf())

	val playingTrack = playerMediaController?.getCurrentTrack() ?: Track(-1, "", "", listOf())
	val playing = playerMediaController?.isPlaying() ?: false

	val libraryTracks by libraryTracksLD.observeAsState(initial = listOf())

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
							if (playingTrack.id == clickedTrack.id) {
								playerMediaController?.transportControls?.play()
							} else {
								onPlay(clickedTrack)
							}
						} else {
							if (playingTrack.id == clickedTrack.id) {
								playerMediaController?.transportControls?.play()
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
					secondaryIcon = android.R.drawable.ic_menu_save
				)
			}
			item {
				Spacer(modifier = Modifier.height(300.dp))
			}
		},

		)
}