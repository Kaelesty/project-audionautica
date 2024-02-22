package com.kaelesty.audionautica.presentation.composables.music

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun PlayerBar(
	onResume: () -> Unit,
	onPause: () -> Unit,
	onNext: () -> Unit,
	onPrev: () -> Unit,
	playingFlow: SharedFlow<Boolean>,
	trackFlow: SharedFlow<Track>,
	pausedTrackId: MutableState<Int>
) {

	val playing by playingFlow.collectAsState(initial = false)
	val track by trackFlow.collectAsState(initial = Track(- 1, "", "", listOf()))
	GradientCard {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 6.dp, vertical = 4.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = track.title + " - " + track.artist,
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.Normal,
				fontSize = 16.sp,
				modifier = Modifier
					.weight(1f)
			)
			Icon(
				imageVector = Icons.Filled.ArrowBack,
				contentDescription = null,
				modifier = Modifier
					.clickable {
						onPrev()
					}
					.size(45.dp)
					.padding(horizontal = 8.dp)
			)
			Icon(
				imageVector = Icons.Filled.ArrowForward,
				contentDescription = null,
				modifier = Modifier
					.clickable {
						onNext()
					}
					.size(45.dp)
					.padding(horizontal = 8.dp)
			)
			Icon(
				imageVector = if (! playing) Icons.Outlined.PlayArrow else Icons.Outlined.Clear,
				contentDescription = null,
				modifier = Modifier
					.clickable {
						if (playing) {
							onPause()
							pausedTrackId.value = track.id
						} else {
							onResume()
						}
					}
					.size(45.dp)
					.padding(horizontal = 8.dp)
			)
		}
	}
}