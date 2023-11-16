package com.kaelesty.audionautica.presentation.composables.music

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme

@Composable
fun TrackSearchResult(
	track: Track,
	onClick: (Track) -> Unit,
	onAdd: (Track) -> Unit,
) {
	Card(
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
		),
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
			.clickable {
				onClick(track)
			}
	) {
		Row(
			modifier = Modifier
				.padding(
					horizontal = 4.dp,
					vertical = 4.dp
				),
			verticalAlignment = Alignment.CenterVertically
		) {
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
					fontSize = 24.sp,
					modifier = Modifier
						.fillMaxWidth()
				)
				Text(
					text = track.artist,
					fontFamily = SpaceGrotesk,
					fontWeight = FontWeight.Normal,
					fontSize = 16.sp,
					modifier = Modifier
						.fillMaxWidth()
				)
				Spacer(Modifier.height(4.dp))
				Row(
					modifier = Modifier
						.height(30.dp)
						.fillMaxWidth()
				) {
					track.tags.forEach { tag ->
						Box(
							modifier = Modifier
								.background(
									color = MaterialTheme.colorScheme.surfaceVariant,
									shape = RoundedCornerShape(10.dp)
								)
						) {
							Text(
								text = tag,
								fontFamily = SpaceGrotesk,
								fontWeight = FontWeight.Light,
								fontSize = 12.sp,
								modifier = Modifier
									.padding(4.dp)
							)
						}
						Spacer(Modifier.width(2.dp))
					}
				}
			}
			Spacer(Modifier.width(12.dp))
			IconButton(
				onClick = { onAdd(track) },
				modifier = Modifier
					.size(40.dp)
			) {
				Icon(
					painterResource(id = android.R.drawable.ic_input_add),
					contentDescription = null,
					modifier = Modifier
						.fillMaxSize()
				)
			}
			Spacer(Modifier.width(12.dp))
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
			tags = listOf("Rock!", "Demon Slayer", "Cover")
		), {}, {})
	}
}