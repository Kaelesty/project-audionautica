package com.kaelesty.audionautica.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.os.BuildCompat
import com.kaelesty.audionautica.di.DaggerApplicationComponent
import com.kaelesty.audionautica.presentation.composables.access.AccessScreen
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.viewmodels.AccessViewModel
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory
import com.kaelesty.audionautica.system.ModifiedApplication
import javax.inject.Inject

class AccessActivity : ComponentActivity() {

	@Inject
	lateinit var viewModelFactory: ViewModelFactory

	private val component by lazy {
		(application as ModifiedApplication).component
	}

	override fun onCreate(savedInstanceState: Bundle?) {

		component.inject(this)

		super.onCreate(savedInstanceState)

		intent.getStringExtra(MESSAGE_EXTRA_KEY)?.let {
			Toast.makeText(
				this@AccessActivity,
				it,
				Toast.LENGTH_LONG
			).show()
		}

		setContent {
			AudionauticaTheme {
				AccessScreen(
					viewModelFactory,
					onFinish = {
						onFinish(it)
					}
				)
			}
		}
	}

	private fun onFinish(mode: AccessViewModel.RunMusicActivityMode) {
		startActivity(
			MusicActivity.newIntent(
				this@AccessActivity,
				when (mode) {
					AccessViewModel.RunMusicActivityMode.ONLINE -> false
					AccessViewModel.RunMusicActivityMode.OFFLINE -> true
				}
			)
		)
		finish()
	}

	companion object {

		private const val MESSAGE_EXTRA_KEY = "IS_OFFLINE"

		fun newIntent(
			context: Context,
			message: String,
		) = Intent(context, AccessActivity::class.java).apply {
			putExtra(MESSAGE_EXTRA_KEY, message)
		}
	}
}