package com.kaelesty.audionautica.presentation.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.kaelesty.audionautica.domain.usecases.GetLogoutFlowUseCase
import com.kaelesty.audionautica.presentation.composables.music.MusicScreen
import com.kaelesty.audionautica.presentation.player.PlayerService
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory
import com.kaelesty.audionautica.system.ModifiedApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicActivity : ComponentActivity() {


	@Inject lateinit var viewModelFactory: ViewModelFactory
	@Inject lateinit var getLogoutFlowUseCase: GetLogoutFlowUseCase

	private val component by lazy {
		(application as ModifiedApplication).component
	}

	private var isOffline: Boolean = false
	private var serviceConn: ServiceConnection? = null

	private val scope = CoroutineScope(Dispatchers.IO)

	private val _requestedImageFlow = MutableSharedFlow<Uri?>()
	val requestedImageFlow: SharedFlow<Uri?> get() = _requestedImageFlow

	val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
		scope.launch {
			_requestedImageFlow.emit(it)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {

		super.onCreate(savedInstanceState)
		component.inject(this)

		scope.launch {
			getLogoutFlowUseCase().collect {
				launchAccessActivity()
			}
		}

		var binder: IBinder?

		serviceConn = object : ServiceConnection {

			override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
				binder = p1 as PlayerService.MusicPlayerServiceBinder
			}

			override fun onServiceDisconnected(p0: ComponentName?) {
				binder = null
			}
		}
		isOffline = intent.getBooleanExtra(IS_OFFLINE_EXTRA_KEY, false)
		bindService(
			PlayerService.newIntent(this@MusicActivity),
			serviceConn as ServiceConnection,
			BIND_AUTO_CREATE
		)

		setContent {
			AudionauticaTheme {
				MusicScreen(
					viewModelFactory = viewModelFactory,
					offlineMode = isOffline,
					launchCreateTrackActivity = { launchAddTrackActivity() },
					onRequestImage = {
						getImage.launch("image/*")
					},
					requestedImageFlow = requestedImageFlow
				)
			}
		}

	}

	private fun launchAddTrackActivity() {
		startActivity(
			AddTrackActivity.newIntent(this@MusicActivity)
		)
	}

	override fun onDestroy() {
		super.onDestroy()
		serviceConn?.let { unbindService(it) }
		scope.cancel()
	}

	private fun launchAccessActivity() {
		startActivity(
			AccessActivity.newIntent(
				this@MusicActivity,
				"Authorization expired!"
			)
		)
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