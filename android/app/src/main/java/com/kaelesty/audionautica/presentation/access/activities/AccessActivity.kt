package com.kaelesty.audionautica.presentation.access.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.kaelesty.audionautica.presentation.access.composables.access.AccessScreen
import com.kaelesty.audionautica.presentation.access.ui.theme.AudionauticaTheme

class AccessActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AudionauticaTheme {
				AccessScreen()
			}
		}
	}
}