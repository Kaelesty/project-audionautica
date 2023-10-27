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
import com.kaelesty.audionautica.system.ModifiedApplication
import javax.inject.Inject
import kotlin.properties.Delegates

class MusicActivity : ComponentActivity() {


	@Inject lateinit var viewModel: MusicViewModel

	private val component by lazy {
		(application as ModifiedApplication).component
	}

	private var isOffline: Boolean = false

	override fun onCreate(savedInstanceState: Bundle?) {

		component.inject(this)

		isOffline = intent.getBooleanExtra(IS_OFFLINE_EXTRA_KEY, false)

		super.onCreate(savedInstanceState)
		setContent {
			AudionauticaTheme {
				MusicScreen(viewModel)
			}
		}
	}

	companion object {
		private const val IS_OFFLINE_EXTRA_KEY = "IS_OFFLINE"

		fun newIntent(
			context: Context,
			isOffline: Boolean = false
		) = Intent(context, MusicActivity::class.java).apply {
			putExtra(IS_OFFLINE_EXTRA_KEY, isOffline)
		}
	}
}