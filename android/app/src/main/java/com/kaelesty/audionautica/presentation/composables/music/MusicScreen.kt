package com.kaelesty.audionautica.presentation.composables.music

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.viewmodels.MusicViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicScreen(
	viewModel: MusicViewModel
) {

	val mode = rememberSaveable {
		mutableStateOf(MusicScreenMode.SEARCH)
	}

	Scaffold(
		modifier = Modifier
			.fillMaxSize(),
		bottomBar = { MusicNavigationBar(mode) },
	) {
		it
		Image(
			painter = painterResource(
				id = R.drawable.space_background
			),
			contentDescription = "Space Background",
			modifier = Modifier.fillMaxSize(),
			contentScale = ContentScale.FillHeight
		)
		when (mode.value) {
			MusicScreenMode.SEARCH -> Search(viewModel)
			MusicScreenMode.PLAYLISTS -> Playlists(viewModel)
			MusicScreenMode.MY_TRACKS -> MyTracks(viewModel)
		}
	}
}

@Composable
fun Playlists(viewModel: MusicViewModel) {

}

@Composable
fun MyTracks(viewModel: MusicViewModel) {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Search(
	viewModel: MusicViewModel
) {
	val tracks by viewModel.tracks.observeAsState(listOf())
	LazyColumn(
		modifier = Modifier
			.fillMaxSize(),
		content = {
			stickyHeader { MusicSearchBar(viewModel) }
			items(tracks, key = { it.id }) {
				TrackSearchResult(it.info)
			}
			item {
				Spacer(modifier = Modifier.height(100.dp))
			}
		},

		)
}

@Composable
fun MusicSearchBar(viewModel: MusicViewModel) {

	var query by rememberSaveable {
		mutableStateOf("")
	}

	GradientCard {
		OutlinedTextField(
			value = query,
			onValueChange = {
				query = it
				viewModel.search(it)
			},
			leadingIcon = {
				Icon(
					Icons.Outlined.Search,
					contentDescription = "Search Icon",
					modifier = Modifier
						.padding(8.dp)

				)
			},
			modifier = Modifier.fillMaxWidth(),
			singleLine = true
		)
	}
}

@Composable
fun MusicNavigationBar(mode: MutableState<MusicScreenMode>) {
	var selectedItem by rememberSaveable {
		mutableStateOf(0)
	}
	val items = listOf("Search", "Playlists", "My Tracks")

	NavigationBar(
		containerColor = Color.Transparent,
		modifier = Modifier.height(100.dp)
	) {
		GradientCard {
			Row(verticalAlignment = Alignment.CenterVertically) {
				items.forEachIndexed { index, item ->
					NavigationBarItem(
						selected = selectedItem == index,
						onClick = {
							selectedItem = index
							when (index) {
								0 -> { mode.value = MusicScreenMode.SEARCH }
								1 -> { mode.value = MusicScreenMode.PLAYLISTS }
								2 -> { mode.value = MusicScreenMode.MY_TRACKS }
							}
						},
						icon = {
							Icon(
								when (item) {
									"Search" -> Icons.Outlined.Search
									"Playlists" -> Icons.Outlined.List
									"My Tracks" -> Icons.Outlined.Edit
									else -> Icons.Outlined.Warning
								},
								"Search Icon"
							)
						},
						modifier = Modifier.height(80.dp)
					)
				}
			}
		}
	}
}