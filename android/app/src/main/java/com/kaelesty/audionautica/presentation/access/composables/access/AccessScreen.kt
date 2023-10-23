package com.kaelesty.audionautica.presentation.access.composables.access

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.presentation.access.ui.fonts.SpaceGrotesk
import com.kaelesty.audionautica.presentation.access.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.access.ui.theme.CardBorderDark

@Composable
fun AccessScreen() {
	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Image(
			painter = painterResource(id = R.drawable.space_background),
			contentDescription = "Background",
			contentScale = ContentScale.FillBounds,
			modifier = Modifier
				.fillMaxSize()
		)

		GradientCard(
			modifier = Modifier.height(400.dp)
		) {
			Register()
		}
	}
}

@Preview
@Composable
fun PreviewLight() {
	AudionauticaTheme(darkTheme = false) {
		AccessScreen()
	}
}

@Preview
@Composable
fun PreviewDark() {
	AudionauticaTheme(darkTheme = true) {
		AccessScreen()
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register() {

	val email = rememberSaveable {
		mutableStateOf("")
	}
	val name = rememberSaveable {
		mutableStateOf("")
	}
	val password = rememberSaveable {
		mutableStateOf("")
	}

	Column(
		modifier = Modifier
			.padding(6.dp)
	) {
		Text(
			text = "Register",
			fontSize = 24.sp,
			fontStyle = FontStyle.Normal,
			fontFamily = SpaceGrotesk,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.onSurface
		)
		Spacer(Modifier.height(20.dp))
		TextInput(email, "Email")
		Spacer(Modifier.height(20.dp))
		TextInput(name, hint = "Name")
		Spacer(Modifier.height(20.dp))
		TextInput(password, hint = "Password", true)
		Spacer(Modifier.height(20.dp))

		OutlinedButton(
			onClick = { /*TODO*/ },
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 90.dp),
			shape = RoundedCornerShape(8.dp)
		) {
			Text(
				"REGISTER",
				color = MaterialTheme.colorScheme.onSurface,
				fontFamily = SpaceGrotesk
			)
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
				color = MaterialTheme.colorScheme.onSurface,
				fontFamily = SpaceGrotesk
			)
		},
		singleLine = true,
		visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
	)
}