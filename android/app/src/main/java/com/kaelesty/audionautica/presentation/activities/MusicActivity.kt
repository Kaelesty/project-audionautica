package com.kaelesty.audionautica.presentation.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.kaelesty.audionautica.presentation.composables.music.MusicScreen
import com.kaelesty.audionautica.presentation.services.MusicPlayerService
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
		super.onCreate(savedInstanceState)
		component.inject(this)

		var playerMediaController: MediaController? = null
		var binder: IBinder? = null

		isOffline = intent.getBooleanExtra(IS_OFFLINE_EXTRA_KEY, false)

		bindService(
			Intent(this@MusicActivity, MusicPlayerService::class.java),
			object: ServiceConnection {

				override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
					binder = p1 as MusicPlayerService.MusicPlayerServiceBinder
					try {
						playerMediaController = MediaController(
							this@MusicActivity,
							(binder as MusicPlayerService.MusicPlayerServiceBinder).getMediasessionToken()
						)
						playerMediaController?.registerCallback(
							object: MediaController.Callback() {
								override fun onPlaybackStateChanged(state: PlaybackState?) {
									super.onPlaybackStateChanged(state)
									state?.let {
										val playing = it.state == PlaybackState.STATE_PLAYING

									}
								}
							}
						)
					}
					catch (e: RemoteException) {
						playerMediaController = null
					}
				}

				override fun onServiceDisconnected(p0: ComponentName?) {
					playerMediaController = null
					binder = null
				}
			},
			BIND_AUTO_CREATE
		)


		val onPause: () -> Unit = {
			Log.d("MusicService", "UI onPause")
			playerMediaController?.transportControls?.pause()
		}
		val onPlay: () -> Unit = {
			Log.d("MusicService", "UI onPlay")
			playerMediaController?.transportControls?.play()
		}
		val onStop: () -> Unit = {
			playerMediaController?.transportControls?.stop()
		}

		setContent {
			AudionauticaTheme {
				MusicScreen(
					viewModel,
					onPlay, onPause, onStop
				)
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