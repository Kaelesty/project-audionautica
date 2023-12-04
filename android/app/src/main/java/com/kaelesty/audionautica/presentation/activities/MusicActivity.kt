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
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.composables.music.MusicScreen
import com.kaelesty.audionautica.presentation.services.MusicPlayerService
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.viewmodels.MusicViewModel
import com.kaelesty.audionautica.system.ModifiedApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicActivity : ComponentActivity() {


	@Inject
	lateinit var viewModel: MusicViewModel

	private val component by lazy {
		(application as ModifiedApplication).component
	}

	private var isOffline: Boolean = false
	private var serviceConn: ServiceConnection? = null

	private val scope = CoroutineScope(Dispatchers.IO)

	private val playingFlow = MutableSharedFlow<Boolean>()
	private val trackFlow = MutableSharedFlow<Track>()

	override fun onCreate(savedInstanceState: Bundle?) {

		super.onCreate(savedInstanceState)
		component.inject(this)

		var playerMediaController: MediaController? = null
		var binder: IBinder? = null

		serviceConn = object : ServiceConnection {

			override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
				binder = p1 as MusicPlayerService.MusicPlayerServiceBinder
				try {
					playerMediaController = MediaController(
						this@MusicActivity,
						(binder as MusicPlayerService.MusicPlayerServiceBinder).getMediasessionToken()
					)
					playerMediaController?.registerCallback(
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
								val track = Track(
									-1,
									metadata?.getString(MediaMetadata.METADATA_KEY_TITLE) ?: return,
									metadata.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: return,
									listOf()
								)
								scope.launch {
									trackFlow.emit(track)
								}
							}
						}
					)
				} catch (e: RemoteException) {
					playerMediaController = null
				}
			}

			override fun onServiceDisconnected(p0: ComponentName?) {
				playerMediaController = null
				binder = null
			}
		}
		isOffline = intent.getBooleanExtra(IS_OFFLINE_EXTRA_KEY, false)
		bindService(
			MusicPlayerService.newIntent(this@MusicActivity),
			serviceConn as ServiceConnection,
			BIND_AUTO_CREATE
		)

		setContent {
			AudionauticaTheme {
				MusicScreen(
					tracksSearchResults = viewModel.tracksSearchResults,
					onSearch = { query -> viewModel.search(query) },
					onPlay = { track ->
						viewModel.playTrack(track)
						playerMediaController?.transportControls?.play()
					},
					onPause = {
						viewModel.pause()
						playerMediaController?.transportControls?.pause()
					},
					onResume = {
						playerMediaController?.transportControls?.play()
					},
					onAddTrackToPlaylist = { track, playlistId ->
						viewModel.addTrackToPlaylist(track, playlistId)
					},
					playingFlow = playingFlow,
					trackFlow = trackFlow,
					onRequestTrackCreation = { launchAddTrackActivity() },
					playlistsLiveData = viewModel.getPlaylists()
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