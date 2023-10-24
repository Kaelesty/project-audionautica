package com.kaelesty.audionautica.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.kaelesty.audionautica.presentation.composables.music.MusicScreen
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.viewmodels.MusicViewModel

class MusicActivity : ComponentActivity() {

	private val viewModel by viewModels<MusicViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AudionauticaTheme {
				MusicScreen(viewModel)
			}
		}
	}

	companion object {
		fun newIntent(context: Context) = Intent(context, MusicActivity::class.java)
	}
}