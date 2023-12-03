package com.kaelesty.audionautica.presentation.composables.addtrack

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.presentation.activities.AddTrackActivity
import com.kaelesty.audionautica.presentation.composables.LoadingDialog
import com.kaelesty.audionautica.presentation.composables.MinimalDialog
import com.kaelesty.audionautica.presentation.composables.access.CollectNamedAction
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.composables.access.MultiparameterInputCard
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun AddTrackScreen(
	errorFlow: SharedFlow<String>,
	loadingFlow: SharedFlow<Boolean>,

	musicFilesList: List<AddTrackActivity.NamedUri>,
	onUpload: (String, String, Uri, List<String>) -> Unit
) {
	val error by errorFlow.collectAsState(initial = "")
	val loading by loadingFlow.collectAsState(initial = false)
	var selectedPositionState = rememberSaveable {
		mutableIntStateOf(0)
	}

	var tags = remember {
		mutableStateListOf<String>()
	}



	val dialogState = rememberSaveable {
		mutableStateOf(false)
	}

	Box(
		modifier = Modifier
			.fillMaxSize(),
	) {
		Image(
			painter = painterResource(id = R.drawable.space_background),
			contentDescription = "Background",
			contentScale = ContentScale.FillBounds,
			modifier = Modifier
				.fillMaxSize()
		)

		if (dialogState.value) {
			MinimalDialog(
				onAcceptRequest = {
					tags.add(it)
					dialogState.value = false
				},
				onDimissRequest = { dialogState.value = false }
			)
		}

		if (loading) {
			LoadingDialog()
		}

		Column {
			MultiparameterInputCard(
				mainAction = CollectNamedAction(
					name = "Upload",
				) { params, _ ->
					onUpload(
						params["Title"] ?: return@CollectNamedAction,
						params["Artist"] ?: return@CollectNamedAction,
						musicFilesList[selectedPositionState.value].uri,
						tags
					)
				},
				parameters = listOf("Title", "Artist"),
				title = "Add new track",
				errorMessage = error,
				lastParameterIsPassword = false,
			)

			TagsList(
				tags,
				dialogState
			)

			FileList(
				musicFilesList,
				selectedPositionState
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TagsList(
	tags: MutableList<String>,
	dialogState: MutableState<Boolean>
) {
	GradientCard {
		Column {
			Text(
				text = "Set tags",
				fontSize = 24.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
			)
			Spacer(Modifier.height(6.dp))
			LazyRow(
				modifier = Modifier
					.height(70.dp)
					.padding(horizontal = 10.dp),
				content = {
					stickyHeader {
						Box(
							modifier = Modifier
								.height(45.dp)
								.background(
									color = MaterialTheme.colorScheme.primaryContainer,
									shape = RoundedCornerShape(16.dp)
								)
								.clickable {
									dialogState.value = true
								}
								.padding(8.dp)
						) {
							Text(
								text = " + ",
								color = MaterialTheme.colorScheme.onSurface,
								fontFamily = SpaceGrotesk,
								fontSize = 18.sp
							)
						}
						Spacer(Modifier.width(4.dp))
					}
					itemsIndexed(tags) { index, item ->
						Row(
							modifier = Modifier
								.fillMaxWidth()
								.clickable {
									tags.remove(item)
								}
						) {
							Box(
								modifier = Modifier
									.height(45.dp)
									.background(
										color = MaterialTheme.colorScheme.primaryContainer,
										shape = RoundedCornerShape(16.dp)
									)
									.padding(8.dp)
							) {
								Text(
									text = item,
									color = MaterialTheme.colorScheme.onSurface,
									fontFamily = SpaceGrotesk,
									fontSize = 18.sp
								)
							}
						}
						Spacer(Modifier.width(4.dp))
					}
				}
			)
		}
	}
}

@Composable
fun FileList(
	tracksList: List<AddTrackActivity.NamedUri>,
	selectedPositionState: MutableState<Int>
) {
	GradientCard {
		Column {
			Text(
				text = "Select file",
				fontSize = 24.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
			)
			LazyColumn(
				modifier = Modifier
					.height(400.dp)
					.padding(horizontal = 10.dp),
				content = {
					itemsIndexed(tracksList) { index, item ->
						Row(
							modifier = Modifier
								.fillMaxWidth()
								.clickable {
									selectedPositionState.value = index
								}
						) {
							Box(
								modifier = Modifier.height(60.dp)
							) {
								if (selectedPositionState.value == index) {
									Icon(
										imageVector = Icons.Filled.Check,
										contentDescription = "Selected"
									)
								}
							}
							Spacer(Modifier.width(12.dp))
							Text(
								item.name,
								color = MaterialTheme.colorScheme.onSurface,
								fontFamily = SpaceGrotesk
							)
						}
					}
				}
			)
		}
	}
}