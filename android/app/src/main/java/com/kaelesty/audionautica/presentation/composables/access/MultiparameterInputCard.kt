package com.kaelesty.audionautica.presentation.composables.access

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme

@Composable
fun MultiparameterInputCard(
	title: String = "Multiparameter Input Card",
	parameters: List<String> = listOf("Parameter1", "Parameter2"),
	mainAction: CollectNamedAction,
	leftAction: SimpleNamedAction? = null,
	rightAction: SimpleNamedAction? = null,
) {

	val parameterStates = mutableMapOf<String, MutableState<String>>()
	for (param in parameters) {
		parameterStates[param] = rememberSaveable {
			mutableStateOf("")
		}
	}

	GradientCard(
	) {
		Column(
			modifier = Modifier
				.padding(6.dp)
		) {
			Text(
				text = title,
				fontSize = 24.sp,
				fontStyle = FontStyle.Normal,
				fontFamily = SpaceGrotesk,
				fontWeight = FontWeight.SemiBold,
				color = colorScheme.onSurface,
				modifier = Modifier
					.padding(horizontal = 10.dp)
			)
			for (param in parameterStates) {
				Spacer(modifier = Modifier.height(12.dp))
				TextInput(
					value = param.value,
					hint = param.key
				)
			}
			Spacer(modifier = Modifier.height(6.dp))
			Row(
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				leftAction?.let {
					Text(
						text = it.name,
						fontFamily = SpaceGrotesk,
						fontSize = 14.sp,
						modifier = Modifier
							.padding(horizontal = 12.dp)
							.clickable {
								it.onAction()
							},
						textDecoration = TextDecoration.Underline,
						color = colorScheme.onSurface
					)
				}
				Spacer(modifier = Modifier.weight(1f))
				rightAction?.let {
					Text(
						text = it.name,
						fontFamily = SpaceGrotesk,
						fontSize = 14.sp,
						modifier = Modifier
							.padding(horizontal = 12.dp)
							.clickable {
								it.onAction()
							},
						textDecoration = TextDecoration.Underline,
						color = colorScheme.onSurface
					)
				}
			}
			Spacer(Modifier.height(20.dp))
			Button(
				onClick = {
					val result = mutableMapOf<String, String>()
					for (param in parameterStates) {
						result[param.key] = param.value.value
					}
					mainAction.onCollect(result)
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 90.dp),
				shape = RoundedCornerShape(8.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = colorScheme.primaryContainer,
					contentColor = colorScheme.surface
				)
			) {
				Text(
					mainAction.name,
					color = colorScheme.onSurface,
					fontFamily = SpaceGrotesk
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(value: MutableState<String>, hint: String, isPassword: Boolean = false) {
	TextField(
		value = value.value,
		onValueChange = {
			value.value = it
		},
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 10.dp),
		label = {
			Text(
				hint,
				color = colorScheme.onSurface,
				fontFamily = SpaceGrotesk
			)
		},
		singleLine = true,
		visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
	)
}

@Composable
fun GradientCard(
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit = {}
) {
	Card(
		modifier = modifier
			.fillMaxWidth()
			.padding(8.dp),
		colors = CardDefaults.cardColors(
			colorScheme.outline.copy(alpha = 0.7f)
		)
	) {
		Box(
			modifier = Modifier
				.padding(8.dp)
				.background(
					brush = Brush.linearGradient(
						listOf(
							MaterialTheme.colorScheme.surface,
							MaterialTheme.colorScheme.surfaceVariant
						)
					),
					shape = RoundedCornerShape(8.dp)
				)
				.fillMaxWidth()
		) {
			content()
		}
	}
}

@Preview
@Composable
fun PreviewMICLight() {
	AudionauticaTheme(darkTheme = false) {
		MultiparameterInputCard(
			mainAction = CollectNamedAction("Action") { },
			leftAction = SimpleNamedAction("Action", {}),
			rightAction = SimpleNamedAction("Action", {})
		)
	}
}

@Preview
@Composable
fun PreviewMICDark() {
	AudionauticaTheme(darkTheme = true) {
		MultiparameterInputCard(
			mainAction = CollectNamedAction("Action") { },
			leftAction = SimpleNamedAction("Action", {}),
			rightAction = SimpleNamedAction("Action", {})
		)
	}
}