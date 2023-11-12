package com.kaelesty.audionautica.presentation.composables.music

import android.graphics.drawable.Icon
import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackInfo
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme

@Composable
fun TrackSearchResult(
	track: Track,
	onPlay: () -> Unit,
	onAdd: () -> Unit,
) {
	Card(
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
		),
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
	) {
		Row(
			modifier = Modifier
				.padding(12.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			AsyncImage(
				model = track.poster,
				contentDescription = "Track Poster",
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.size(100.dp)
					.clip(RoundedCornerShape(6.dp))
			)
			Spacer(Modifier.width(12.dp))
			Column(
				modifier = Modifier
					.align(Alignment.CenterVertically)
					.weight(1f)
			) {
				Text(
					text = track.title,
					fontFamily = SpaceGrotesk,
					fontWeight = FontWeight.ExtraBold,
					fontSize = 30.sp
				)
				Text(
					text = track.artist,
					fontFamily = SpaceGrotesk,
					fontWeight = FontWeight.Normal,
					fontSize = 18.sp
				)
			}
			Spacer(Modifier.width(12.dp))
			IconButton(
				onClick = { onPlay() },
				modifier = Modifier
					.size(40.dp)
					.background(
						shape = CircleShape,
						color = MaterialTheme.colorScheme.surfaceVariant
					)
			) {
				Icon(
					painterResource(id = android.R.drawable.ic_media_play),
					contentDescription = null,
					modifier = Modifier
						.fillMaxSize()
				)
			}
			Spacer(Modifier.width(12.dp))
			IconButton(
				onClick = { onAdd() },
				modifier = Modifier
					.size(40.dp)
					.background(
						shape = CircleShape,
						color = MaterialTheme.colorScheme.surfaceVariant
					)
			) {
				Icon(
					painterResource(id = android.R.drawable.ic_input_add),
					contentDescription = null,
					modifier = Modifier
						.fillMaxSize()
				)
			}
		}
	}
}

@Preview
@Composable
fun TrackSearchResultPreview() {
	AudionauticaTheme {
		TrackSearchResult(track = Track(
			id = 1,
			title = "Akeboshi",
			artist = "re:Tye",
			poster = "https://i.pinimg.com/564x/ee/0a/ea/ee0aea8e9bcfdef2eefde9cbbb174967.jpg"
		), {}, {})
	}
}