package com.kaelesty.audionautica.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import com.kaelesty.audionautica.presentation.composables.access.AccessScreen
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.viewmodels.AccessViewModel

class AccessActivity : ComponentActivity() {

	val viewModel by viewModels<AccessViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {

		viewModel.runMusicActivity.observe(this@AccessActivity) {
			startActivity(MusicActivity.newIntent(this@AccessActivity))
		}

		super.onCreate(savedInstanceState)
		setContent {
			AudionauticaTheme {
				AccessScreen(viewModel)
			}
		}
	}
}