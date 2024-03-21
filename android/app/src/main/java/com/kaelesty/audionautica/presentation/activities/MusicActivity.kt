package com.kaelesty.audionautica.presentation.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.composables.music.MusicScreen
import com.kaelesty.audionautica.presentation.player.PlayerService
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.viewmodels.MusicViewModel
import com.kaelesty.audionautica.presentation.viewmodels.ViewModelFactory
import com.kaelesty.audionautica.system.ModifiedApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicActivity : ComponentActivity() {


	@Inject lateinit var viewModelFactory: ViewModelFactory

	private val component by lazy {
		(application as ModifiedApplication).component
	}

	private var isOffline: Boolean = false
	private var serviceConn: ServiceConnection? = null

	private val scope = CoroutineScope(Dispatchers.IO)

	private val playingFlow = MutableSharedFlow<Boolean>()
	private val trackFlow = MutableSharedFlow<Track>()

	private val _playerMediaControllerFlow: MutableStateFlow<MediaController?> = MutableStateFlow(null)
	val playerMediaControllerFlow: StateFlow<MediaController?> get() = _playerMediaControllerFlow.asStateFlow()

	override fun onCreate(savedInstanceState: Bundle?) {

		super.onCreate(savedInstanceState)
		component.inject(this)

		var binder: IBinder?

		serviceConn = object : ServiceConnection {

			override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
				binder = p1 as PlayerService.MusicPlayerServiceBinder
				try {
					val playerMediaController = MediaController(
						this@MusicActivity,
						(binder as PlayerService.MusicPlayerServiceBinder).getMediasessionToken()
					)
					playerMediaController.registerCallback(
						object : MediaController.Callback() {
							override fun onPlaybackStateChanged(state: PlaybackState?) {
								super.onPlaybackStateChanged(state)
								state?.let {
									val playing = it.state == PlaybackState.STATE_PLAYING
									scope.launch {
										playingFlow.emit(playing)
									}
								}
							}

							override fun onMetadataChanged(metadata: MediaMetadata?) {
								super.onMetadataChanged(metadata)
								try {
									val track = Track(
										metadata?.getLong(PlayerService.CUSTOM_METADATA_KEY_ID)?.toInt() ?: throw RuntimeException("Metadata id not found"),
										metadata.getString(MediaMetadata.METADATA_KEY_TITLE),
										metadata.getString(MediaMetadata.METADATA_KEY_ARTIST),
										metadata.getString(PlayerService.CUSTOM_METADATA_KEY_TAGS)
											.split(PlayerService.CUSTOM_METADATA_TAGS_DELIMITER)
									)
									scope.launch {
										trackFlow.emit(track)
									}
								}
								catch (e: RuntimeException) {
									Log.d("AudionauticaTag", e.message ?: "no message")
								}
							}
						}
					)
					scope.launch { _playerMediaControllerFlow.emit(playerMediaController) }
				} catch (e: RemoteException) {
					scope.launch { _playerMediaControllerFlow.emit(null) }
				}
			}

			override fun onServiceDisconnected(p0: ComponentName?) {
				scope.launch { _playerMediaControllerFlow.emit(null) }
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
					playerMediaControllerFlow = playerMediaControllerFlow
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