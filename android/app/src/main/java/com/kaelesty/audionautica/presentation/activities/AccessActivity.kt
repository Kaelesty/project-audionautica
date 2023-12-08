package com.kaelesty.audionautica.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.kaelesty.audionautica.di.DaggerApplicationComponent
import com.kaelesty.audionautica.presentation.composables.access.AccessScreen
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.viewmodels.AccessViewModel
import com.kaelesty.audionautica.system.ModifiedApplication
import javax.inject.Inject

class AccessActivity : ComponentActivity() {

	@Inject lateinit var viewModel: AccessViewModel

	private val component by lazy {
		(application as ModifiedApplication).component
	}

	override fun onCreate(savedInstanceState: Bundle?) {

		component.inject(this)


		viewModel.runMusicActivity.observe(this@AccessActivity) {
			startActivity(
				MusicActivity.newIntent(
					this@AccessActivity,
					when(it) {
						AccessViewModel.RunMusicActivityMode.ONLINE -> false
						AccessViewModel.RunMusicActivityMode.OFFLINE -> true
					}
				)
			)
			finish()
		}

		viewModel.checkConnectionAndAuth()

		super.onCreate(savedInstanceState)
		setContent {
			AudionauticaTheme {
				AccessScreen(viewModel)
			}
		}
	}
}