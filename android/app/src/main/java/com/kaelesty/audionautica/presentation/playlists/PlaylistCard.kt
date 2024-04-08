package com.kaelesty.audionautica.presentation.playlists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kaelesty.audionautica.domain.playlists.Playlist
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.shimmering.shimmerLoadingAnimation
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk

@Composable
fun PlaylistCard(
	state: PlaylistCardState
) {

	GradientCard(
		modifier = Modifier.fillMaxWidth()
	) {
		Row(
			modifier = Modifier
				.padding(6.dp)
		) {
			when (state) {
				is PlaylistCardState.IdOnly -> {
					Box(
						modifier = Modifier
							.size(120.dp)
							.clip(shape = RoundedCornerShape(10.dp))
							.shimmerLoadingAnimation(
								isLoadingCompleted = false
							)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Column(
						modifier = Modifier.height(120.dp),
					) {
						Spacer(modifier = Modifier.height(8.dp))
						Box(modifier = Modifier
							.height(24.dp)
							.width(200.dp)
							.clip(shape = RoundedCornerShape(10.dp))
							.shimmerLoadingAnimation(
								isLoadingCompleted = false
							)
						)
						Spacer(modifier = Modifier.height(8.dp))
						Box(modifier = Modifier
							.height(24.dp)
							.width(150.dp)
							.clip(shape = RoundedCornerShape(10.dp))
							.shimmerLoadingAnimation(
								isLoadingCompleted = false
							)
						)
					}
				}

				is PlaylistCardState.Full -> {
					val playlist = state.playlist
					AsyncImage(
						model = playlist.imageUrl,
						contentDescription = "playlist image",
						modifier = Modifier
							.size(120.dp)
							.clip(
								RoundedCornerShape(8.dp)
							)
						,
						contentScale = ContentScale.Crop,
					)
					Spacer(Modifier.width(18.dp))
					Column(
						modifier = Modifier.height(120.dp),
					) {
						Spacer(Modifier.weight(1f))
						Text(
							playlist.title,
							color = MaterialTheme.colorScheme.onSurface,
							fontFamily = SpaceGrotesk,
							fontSize = 24.sp,
							fontWeight = FontWeight.Bold
						)
						Spacer(modifier = Modifier.height(4.dp))
						Text(
							playlist.desc,
							color = MaterialTheme.colorScheme.onSurface,
							fontFamily = SpaceGrotesk,
							fontSize = 14.sp,
						)
						Spacer(Modifier.weight(1f))
					}
					Spacer(modifier = Modifier.width(12.dp))
					Box(
						modifier = Modifier.height(120.dp),
						contentAlignment = Alignment.Center
					) {
						IconButton(
							onClick = { /*TODO*/ },
							modifier = Modifier.size(30.dp)
						) {
							Icon(Icons.Filled.ExitToApp, contentDescription = "details of playlist")
						}
					}
				}
			}


		}
	}
}