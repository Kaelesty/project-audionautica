package com.kaelesty.audionautica.presentation.composables.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.audionautica.domain.entities.TrackInfo
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme

@Composable
fun TrackSearchResult(
	info: TrackInfo
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
				.padding(12.dp)
			,
			verticalAlignment = Alignment.CenterVertically
		) {
			Image(
				painter = painterResource(id = info.poster),
				contentDescription = "Track Poster",
				modifier = Modifier
					.size(100.dp)
					.clip(RoundedCornerShape(4.dp)),
			)
			Spacer(Modifier.width(12.dp))
			Column(
				modifier = Modifier
					.align(Alignment.CenterVertically)
					.weight(1f)
			) {
				Text(
					text = info.name,
					fontFamily = SpaceGrotesk,
					fontWeight = FontWeight.ExtraBold,
					fontSize = 35.sp
				)
				Text(
					text = info.artist,
					fontFamily = SpaceGrotesk,
					fontWeight = FontWeight.Normal,
					fontSize = 20.sp
				)
			}
			Spacer(Modifier.width(12.dp))
			Icon(
				Icons.Outlined.MoreVert,
				contentDescription = "More",
				modifier = Modifier.size(40.dp)
			)
		}
	}
}

@Preview
@Composable
fun TrackSearchResultPreview() {
	AudionauticaTheme {
		TrackSearchResult(info = TrackInfo())
	}
}