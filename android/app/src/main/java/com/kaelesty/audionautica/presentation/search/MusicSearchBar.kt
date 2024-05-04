package com.kaelesty.audionautica.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kaelesty.audionautica.presentation.composables.access.GradientCard
import com.kaelesty.audionautica.presentation.getFont

@Composable
fun MusicSearchBar(
	vm: SearchViewModel
) {

	var query by rememberSaveable {
		mutableStateOf("")
	}

	var selectedIndex by remember { mutableStateOf(0) }
	val searchOptions = listOf(SearchType.Tracks, SearchType.Playlists)
	var expanded by remember { mutableStateOf(false) }


	GradientCard {
		OutlinedTextField(
			value = query,
			onValueChange = {
				query = it
				vm.search(query, searchOptions[selectedIndex])
			},
			leadingIcon = {
				Icon(
					Icons.Outlined.Search,
					contentDescription = "Search Icon",
					modifier = Modifier
						.padding(8.dp)

				)
			},
			trailingIcon = {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.End,
					modifier = Modifier
						.width(100.dp)
				) {
					Divider(
						color = MaterialTheme.colorScheme.primary,
						modifier = Modifier
							.padding(vertical = 2.dp, horizontal = 4.dp)
							.height(40.dp)
							.width(2.dp)
							,
						thickness = 2.dp
					)
					Text(
						text = searchOptions[selectedIndex].name,
						fontFamily = getFont(),
						modifier = Modifier
							.clickable { expanded = ! expanded }
							.padding(horizontal = 4.dp)
							.weight(1f),
						textAlign = TextAlign.Center
					)
				}
				DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
					searchOptions.forEachIndexed { index, searchType ->
						DropdownMenuItem(
							text = {
								Text(text = searchType.name, fontFamily = getFont())
							},
							onClick = {
								if (selectedIndex != index) {
									vm.search(query, searchType)
								}
								selectedIndex = index
								expanded = false
							}
						)
					}
				}
			},
			modifier = Modifier.fillMaxWidth(),
			singleLine = true
		)
	}
}