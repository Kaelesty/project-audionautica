package com.kaelesty.audionautica.presentation.player

import android.media.session.MediaController
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory
import java.time.temporal.ValueRange

@Composable
fun PlayerBar(
	playerMediaController: MediaController,
	viewModelFactory: ViewModelFactory
) {

	val vm: PlayerViewModel = viewModel(factory = viewModelFactory)

	vm.setMediaController(playerMediaController)


	val playerState by vm.playerState.collectAsState()
	val track = playerState.meta ?: Track(-1, "None", "", listOf())

	var progress = playerState.progress

	val playing = playerState.playing
	val duration = playerState.duration

	val left = duration - progress

	val leftMinutes = (left / 60000).toInt()
	val leftSeconds = (left / 1000).toInt() - leftMinutes * 60

	GradientCard {
		Column {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 6.dp, vertical = 4.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = "${track.title} - ${track.artist}",
					fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) SpaceGrotesk else FontFamily.SansSerif,
					fontWeight = FontWeight.Normal,
					fontSize = 16.sp,
					modifier = Modifier
						.weight(1f)
				)
				Text(
					text = "-$leftMinutes:${if (leftSeconds != 0) leftSeconds else "00"}",
					fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) SpaceGrotesk else FontFamily.SansSerif,
					fontWeight = FontWeight.Thin,
					fontSize = 14.sp,
				)
				Box(
					modifier = Modifier
						.size(51.dp),
					contentAlignment = Alignment.Center
				) {
					// backward and forward icons has different sizes (idk),
					// so we use box to keep paddings after resize
					Icon(
						painter = painterResource(id = R.drawable.backward),
						contentDescription = null,
						modifier = Modifier
							.clickable {
								playerMediaController.transportControls.skipToPrevious()
							}
							.size(45.dp)
							.padding(horizontal = 8.dp)
					)
				}
				Icon(
					painter = painterResource(id = if (playing) R.drawable.pause else R.drawable.play),
					contentDescription = null,
					modifier = Modifier
						.clickable {
							playerMediaController.transportControls.let {
								if (playing) {
									it.pause()
								} else {
									it.play()
								}
							}
						}
						.size(45.dp)
						.padding(horizontal = 8.dp)
				)
				Icon(
					painter = painterResource(id = R.drawable.forward),
					contentDescription = null,
					modifier = Modifier
						.clickable {
							playerMediaController.transportControls.fastForward()
						}
						.size(51.dp)
						.padding(horizontal = 8.dp)
				)
			}
			Slider(
				value = progress.toFloat(),
				onValueChange = {
					vm.setPlayingProgress(it)
				},
				modifier = Modifier
					.padding(horizontal = 6.dp),
				valueRange = 0f..duration.toFloat(),
			)
		}
	}
}