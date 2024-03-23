package com.kaelesty.audionautica.presentation.composables.music

import android.media.session.MediaController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.composables.dialogues.ConfirmTrackDeleteDialog
import com.kaelesty.audionautica.presentation.player.getCurrentTrack
import com.kaelesty.audionautica.presentation.player.isPlaying
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun MyTracks(
	onCreateTrack: () -> Unit,
	libraryTracks: LiveData<List<Track>>,
	onPlay: (Track) -> Unit,
	selectPlaylistDialog: MutableState<Track?>,
	onDeleteTrack: (Track) -> Unit,
	offlineMode: Boolean,
	playerMediaController: MediaController?
) {

	val tracks by libraryTracks.observeAsState(initial = listOf())

	val playing = playerMediaController?.isPlaying() ?: false
	val playingTrack = playerMediaController?.getCurrentTrack() ?: Track(-1, "", "", listOf())

	var deleteTrackDialog by rememberSaveable {
		mutableStateOf<Track?>(null)
	}

	deleteTrackDialog?.let { trackToDelete ->
		ConfirmTrackDeleteDialog(
			onAcceptRequest = {
				deleteTrackDialog = null
				onDeleteTrack(it)
			},
			onDismissRequest = { deleteTrackDialog = null },
			track = trackToDelete
		)
	}

	LazyColumn(
		modifier = Modifier.fillMaxSize(),
		content = {
			items(tracks) { track ->
				TrackCard(
					track = track,
					onClick = { clickedTrack ->
						if (playing) {
							if (track.id == clickedTrack.id) {
								playerMediaController?.transportControls?.pause()
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
					onSecondary = {
						deleteTrackDialog = it
					},
					secondaryIcon = android.R.drawable.ic_menu_delete
				)
			}
		}
	)

	if (!offlineMode) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(12.dp),
			horizontalAlignment = Alignment.End
		) {
			Spacer(Modifier.weight(1f))
			IconButton(
				onClick = { onCreateTrack() },
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
			Spacer(Modifier.height(230.dp))
		}
	}
}