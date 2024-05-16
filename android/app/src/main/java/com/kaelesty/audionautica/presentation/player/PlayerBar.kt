package com.kaelesty.audionautica.presentation.player

import android.media.session.MediaController
import android.os.Build
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.kaelesty.audionautica.domain.player.toInt
import com.kaelesty.audionautica.domain.player.toRepeatMode
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory
import java.math.BigDecimal
import java.time.temporal.ValueRange
import kotlin.math.log

@Composable
fun PlayerBar(
	viewModelFactory: ViewModelFactory
) {

	val vm: PlayerViewModel = viewModel(factory = viewModelFactory)

	val pc = vm.pc

	val playerState by vm.playerState.collectAsState()
	val track = playerState.meta ?: Track(- 1, "", "", listOf())

	var progress = playerState.progress
	Log.d("PlayerBar", progress.toString())
	var isSeeking by remember {
		mutableStateOf(false)
	}
	var seekingProgress by remember {
		mutableFloatStateOf(0f)
	}

	val playing = playerState.playing
	val duration = playerState.duration

	val left = duration - if (isSeeking) seekingProgress.toLong() else progress

	val leftMinutes = (left / 60000).toInt()
	val leftSeconds = (left / 1000).toInt() - leftMinutes * 60

	var expanded by rememberSaveable {
		mutableStateOf(false)
	}

	var speedSliderPosition by rememberSaveable {
		mutableFloatStateOf(100f)
	}

	GradientCard(
		modifier = Modifier
			.clickable {
				expanded = ! expanded
			}
			.animateContentSize()
	) {
		Column {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 6.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = "${track.title} - ${track.artist}",
					fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) SpaceGrotesk else FontFamily.SansSerif,
					fontWeight = FontWeight.Normal,
					fontSize = 18.sp,
					modifier = Modifier
						.weight(1f)
				)
				Text(
					text = "-$leftMinutes:${if (leftSeconds >= 10) leftSeconds else "0$leftSeconds"}",
					fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) SpaceGrotesk else FontFamily.SansSerif,
					fontWeight = FontWeight.Thin,
					fontSize = 14.sp,
					modifier = Modifier
						.width(40.dp)
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
								pc.playPrev()
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
							if (playing) {
								pc.pause()
							} else {
								pc.play()
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
							pc.playNext()
						}
						.size(51.dp)
						.padding(horizontal = 8.dp)
				)
			}
			val sliderVal = if (isSeeking) seekingProgress else progress.toFloat()
			Slider(
				value = if (sliderVal > 0) sliderVal else 0f,

				onValueChange = {
					isSeeking = true
					seekingProgress = it
				},
				modifier = Modifier
					.padding(horizontal = 6.dp),
				valueRange = 0f .. duration.toFloat(),
				onValueChangeFinished = {
					pc.seekTo(seekingProgress.toLong())
					isSeeking = false
				}
			)
			if (expanded) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier.padding(16.dp)
				) {
					Slider(
						value = speedSliderPosition,
						onValueChange = {
							speedSliderPosition = it
						},
						onValueChangeFinished = {
							pc.setPlaybackSpeed(speedSliderPosition / 100)
						},
						valueRange = 50f .. 200f,
						steps = 14,
						modifier = Modifier.weight(1f)
					)
					Text(
						text = "${String.format("%.1f", speedSliderPosition / 100)}x",
						fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) SpaceGrotesk else FontFamily.SansSerif,
						fontWeight = FontWeight.Thin,
						fontSize = 18.sp,
						modifier = Modifier
							.width(50.dp)
					)

					var repeatState by rememberSaveable {
						mutableStateOf(playerState.repeat.toInt())
					}

					Icon(
						painter = painterResource(
							id = when (repeatState) {
								0 -> R.drawable.repeat_no
								1 -> R.drawable.repeat_yes
								else -> R.drawable.repeat_one
							}
						),
						contentDescription = null,
						modifier = Modifier
							.clickable {
								repeatState += 1
								if (repeatState == 3) repeatState = 0
								pc.setRepeat(repeatState.toRepeatMode())
							}
							.size(51.dp)
							.padding(horizontal = 8.dp)
					)
					Icon(
						painter = painterResource(
							id = if (playerState.shuffle) R.drawable.shuffle_yes
							else R.drawable.shuffle_no
						),
						contentDescription = null,
						modifier = Modifier
							.clickable {
								pc.setShuffle(!playerState.shuffle)
							}
							.size(51.dp)
							.padding(horizontal = 8.dp)
					)
				}
				LazyColumn(
					modifier = Modifier
						.fillMaxSize()
						.padding(16.dp)
				) {
					itemsIndexed(
						playerState.queue ?: listOf(),
						key = { index, it ->
							it.id
						}
					) { index, it ->
						Text(
							text = "${it.title} - ${it.artist}",
							fontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) SpaceGrotesk else FontFamily.SansSerif,
							fontWeight = if (index == playerState.currentQueuePosition) FontWeight.ExtraBold else FontWeight.Thin,
							fontSize = 18.sp,
							modifier = Modifier
								.fillMaxWidth()
								.clickable {
									pc.playFromPosition(index)
								}
						)
						Spacer(Modifier.height(6.dp))
					}
				}
			}
		}
	}
}