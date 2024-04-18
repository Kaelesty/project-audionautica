package com.kaelesty.audionautica.presentation.composables.dialogues

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme

@Preview
@Composable
fun previewMinimalDialog() {
	AudionauticaTheme {
		MinimalDialog(onAcceptRequest = {}, {})
	}
}

@Composable
fun MinimalDialog(
	onAcceptRequest: (String) -> Unit,
	onDismissRequest: () -> Unit,
	title: String = "Enter your tag"
	) {
	Dialog({ onDismissRequest() }) {

		val text = rememberSaveable {
			mutableStateOf("")
		}

		Card(
			modifier = Modifier
				.fillMaxWidth()
				.height(200.dp)
				.padding(16.dp),
			shape = RoundedCornerShape(16.dp),
		) {
			Column(
				modifier = Modifier.padding(8.dp)
			) {
				Text(
					text = title,
					fontSize = 24.sp,
					fontStyle = FontStyle.Normal,
					fontFamily = SpaceGrotesk,
					fontWeight = FontWeight.SemiBold,
					color = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier
						.padding(horizontal = 10.dp)
						.padding(top = 8.dp)
				)
				TextField(
					modifier = Modifier
						.padding(horizontal = 8.dp)
						.fillMaxWidth(),
					value = text.value,
					onValueChange = { text.value = it }
				)

				Row(
					Modifier.padding(vertical = 12.dp)
				) {
					Spacer(Modifier.weight(1f))
					Text(
						modifier = Modifier
							.padding(horizontal = 12.dp)
							.clickable {
								onAcceptRequest(text.value)
							},
						text = "OK",
						textAlign = TextAlign.End,
						fontSize = 20.sp,
						fontFamily = SpaceGrotesk,
					)
				}
			}
		}
	}
}