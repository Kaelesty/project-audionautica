package com.kaelesty.audionautica.presentation.playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelesty.audionautica.domain.playlists.Playlist
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory

@Composable
fun Playlists(
	viewModelFactory: ViewModelFactory,
	navigateToAddScreen: () -> Unit,
) {
	val vm = viewModelFactory.create(PlaylistsViewModel::class.java)

	val playlists = listOf(
		Playlist(
			id = 0,
			creatorId = 0,
			title = "Playlist for cats",
			desc = "Do not listen if are not a cat!",
			imageUrl = "https://i.pinimg.com/564x/86/15/50/8615509334c99ba0c11a9feac151a79e.jpg",
			isPrivate = false,
			trackIds = listOf()
		),
		Playlist(
			id = 1,
			creatorId = 0,
			title = "Evil music",
			desc = "I'm glad that you're evil too:)",
			imageUrl = "https://avatars.yandex.net/get-music-user-playlist/30088/791514790.1022.48240/m1000x1000?1678655770433",
			isPrivate = false,
			trackIds = listOf()
		),
	)

	LazyColumn(
		modifier = Modifier
			.padding(12.dp)
			.fillMaxHeight(),
		content = {
			items(
				items = playlists,
				key = { it.id }
			) {
				PlaylistCard(playlist = it)
			}
		}
	)
	Row(
		modifier = Modifier
			.fillMaxSize()
			.padding(bottom = 242.dp, end = 12.dp)
		// 192.dp to make position equals with button of MyTracks
		,
		verticalAlignment = Alignment.Bottom
	) {
		Spacer(Modifier.weight(1f))
		IconButton(
			onClick = {
				navigateToAddScreen()
			},
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
	}
}