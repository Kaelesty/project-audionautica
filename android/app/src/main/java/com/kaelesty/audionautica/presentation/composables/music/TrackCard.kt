package com.kaelesty.audionautica.presentation.composables.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.domain.entities.TrackInfo
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme

@Composable
fun TrackCard(
	onPlay: (() -> Unit)? = null,
	onPause: (() -> Unit)? = null,
	onStop: (() -> Unit)? = null,
) {
	Card(
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
		),
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
	) {
		Column(
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
				.fillMaxWidth()
		) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.size(350.dp)
					.padding(12.dp)
				,
				contentAlignment = Alignment.Center
			) {
				Image(
					painter = painterResource(id = R.drawable.example_track_poster_2),
					contentDescription = "Track Poster",
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.size(350.dp)
						.clip(RoundedCornerShape(12.dp))
						,
				)
			}
			Text(
				text = "Akeboshi",
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.Bold,
				fontSize = 30.sp,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 12.dp)
			)
			Text(
				text = "re:Tye",
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.Bold,
				fontSize = 20.sp,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 12.dp)
			)
			Row(
				modifier = Modifier
					.padding(12.dp)
					.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center,
			) {
				IconButton(
					onClick = { onPlay?.let { it() } },
					Modifier.padding(6.dp)
				) {
					Icon(
						painter = painterResource(
							id = com.google.android.exoplayer2.R.drawable.exo_controls_play
						),
						contentDescription = "Play button",
						modifier = Modifier.background(
							color = MaterialTheme.colorScheme.primaryContainer,
							shape = CircleShape
						),
						tint = MaterialTheme.colorScheme.onSurface
					)
				}
				IconButton(
					onClick = { onPause?.let { it() } },
					Modifier.padding(6.dp)
				) {
					Icon(
						painter = painterResource(
							id = com.google.android.exoplayer2.R.drawable.exo_controls_pause
						),
						contentDescription = "Pause button",
						modifier = Modifier.background(
							color = MaterialTheme.colorScheme.primaryContainer,
							shape = CircleShape
						),
						tint = MaterialTheme.colorScheme.onSurface
					)
				}
			}
		}
	}
}

@Preview
@Composable
fun TrackCardPreview() {
	AudionauticaTheme {
		TrackCard()
	}
}