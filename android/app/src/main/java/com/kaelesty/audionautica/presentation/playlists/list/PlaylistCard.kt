package com.kaelesty.audionautica.presentation.playlists.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.getFont
import com.kaelesty.audionautica.presentation.shimmering.shimmerLoadingAnimation

@Composable
fun PlaylistCard(
	state: UserPlaylist,
	onShowPlaylistDetails: (UserPlaylist.Entire) -> Unit
) {

	Card(
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
		),
		modifier = Modifier
			.fillMaxWidth()
	) {
		when (state) {
			is UserPlaylist.IdOnly -> {
				Row(
					modifier = Modifier
						.padding(6.dp)
				) {
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
						Box(
							modifier = Modifier
								.height(24.dp)
								.width(200.dp)
								.clip(shape = RoundedCornerShape(10.dp))
								.shimmerLoadingAnimation(
									isLoadingCompleted = false
								)
						)
						Spacer(modifier = Modifier.height(8.dp))
						Box(
							modifier = Modifier
								.height(24.dp)
								.width(150.dp)
								.clip(shape = RoundedCornerShape(10.dp))
								.shimmerLoadingAnimation(
									isLoadingCompleted = false
								)
						)
					}
				}

			}

			is UserPlaylist.Entire -> {
				EntirePlaylistCard(
					state,
					onDetails = { onShowPlaylistDetails(state) }
				)
			}
		}
	}
}

@Composable
fun EntirePlaylistCard(
	playlist: UserPlaylist.Entire,
	onDetails: () -> Unit,
) {
	Row(
		modifier = Modifier
			.padding(6.dp)
			.clickable { onDetails() }
	) {
		AsyncImage(
			model = playlist.imageUrl,
			contentDescription = "playlist image",
			modifier = Modifier
				.size(120.dp)
				.clip(
					RoundedCornerShape(8.dp)
				),
			contentScale = ContentScale.Crop,
		)
		Spacer(Modifier.width(18.dp))
		Column(
			modifier = Modifier
				.weight(1f)
				.fillMaxHeight(),
		) {
			Text(
				playlist.title,
				color = MaterialTheme.colorScheme.onSurface,
				fontFamily = getFont(),
				fontSize = 24.sp,
				fontWeight = FontWeight.ExtraBold,
			)
			Text(
				playlist.desc,
				color = MaterialTheme.colorScheme.onSurface,
				fontFamily = getFont(),
				fontSize = 20.sp,
				fontWeight = FontWeight.Normal
			)
			Spacer(modifier = Modifier.height(4.dp))
			Text(
				"${playlist.trackIds.size} Tracks",
				color = MaterialTheme.colorScheme.onSurface,
				fontFamily = getFont(),
				fontSize = 18.sp,
				fontWeight = FontWeight.Normal
			)
			Text(
				"${playlist.likes} Likes",
				color = MaterialTheme.colorScheme.onSurface,
				fontFamily = getFont(),
				fontSize = 18.sp,
				fontWeight = FontWeight.Normal
			)
			Spacer(Modifier.weight(1f))
		}
		Spacer(modifier = Modifier.width(12.dp))
	}

}

//@Preview
//@Composable
//fun EntirePlaylistCardPreview() {
//	AudionauticaTheme {
//		EntirePlaylistCard(
//			playlist = UserPlaylist.Entire(
//				id = 0,
//				title = "Preview Playlist",
//				isEditable = false,
//				isPrivate = false,
//				desc = "Playlist to preview EntirePlaylistCard composable function",
//				trackIds = listOf(),
//				imageUrl = "",
//				likes = 0
//			)
//		)
//	}
//}