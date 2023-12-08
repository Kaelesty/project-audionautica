package com.kaelesty.audionautica.presentation.composables.dialogues

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme

@Composable
fun LoadingDialog(
	) {
	Dialog({}) {

		val text = rememberSaveable {
			mutableStateOf("")
		}

		Card(
			modifier = Modifier
				.width(200.dp)
				.height(200.dp)
				.padding(16.dp),
			shape = RoundedCornerShape(16.dp),
		) {
			Box(modifier = Modifier
				.align(Alignment.CenterHorizontally)
				.fillMaxSize()
			) {
				CircularProgressIndicator(
					modifier = Modifier.fillMaxSize().padding(18.dp)
				)
			}
		}
	}
}

@Preview
@Composable
fun preview(){
	AudionauticaTheme {
		LoadingDialog()
	}
}