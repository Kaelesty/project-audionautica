package com.kaelesty.audionautica.presentation.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackExp
import com.kaelesty.audionautica.domain.entities.TracksToPlay
import com.kaelesty.audionautica.domain.usecases.GetPlayingEndedFlowUseCase
import com.kaelesty.audionautica.domain.usecases.GetPlayingTrackFlowUseCase
import com.kaelesty.audionautica.domain.usecases.GetTrackQueueUseCase
import com.kaelesty.audionautica.domain.usecases.GetTrackUriUseCase
import com.kaelesty.audionautica.presentation.activities.MusicActivity
import com.kaelesty.audionautica.system.ModifiedApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class MusicPlayerService : Service() {

	private val component by lazy {
		(application as ModifiedApplication).component
	}


	private val player by lazy {
		ExoPlayer
			.Builder(this@MusicPlayerService)
			.build()
	}

	private val audioManager by lazy {
		getSystemService(Context.AUDIO_SERVICE) as AudioManager
	}

	val CHANNEL_ID = "MusicPlayerService"
	val CHANNEL_NAME = "MusicPlayerService"


	val scope = CoroutineScope(Dispatchers.IO)

	val metadataBuilder = MediaMetadata.Builder()
	val stateBuilder = PlaybackState
		.Builder()
		.setActions(
			PlaybackState.ACTION_PLAY
					or PlaybackState.ACTION_STOP
					or PlaybackState.ACTION_PAUSE
					or PlaybackState.ACTION_PLAY_PAUSE
					or PlaybackState.ACTION_SKIP_TO_NEXT
					or PlaybackState.ACTION_SKIP_TO_PREVIOUS
		)

	lateinit var mediaSession: MediaSession

	private var nullTrack = true

	@Inject lateinit var getTrackQueueUseCase: GetTrackQueueUseCase
	@Inject lateinit var getTrackUriUseCase: GetTrackUriUseCase

	@Inject lateinit var getPlayingEndedFlowUseCase: GetPlayingEndedFlowUseCase
	@Inject lateinit var getPlayingTrackFlowUseCase: GetPlayingTrackFlowUseCase

	private val audioFocusChangeListener: OnAudioFocusChangeListener = object: OnAudioFocusChangeListener {
		override fun onAudioFocusChange(focusChange: Int) {
			when (focusChange) {
				AudioManager.AUDIOFOCUS_GAIN -> {
					mediaSessionCallback.onPlay()
				}
				AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
					mediaSessionCallback.onPause()
				}
				else -> {
					mediaSessionCallback.onPause()
				}
			}
		}
	}

	private val mediaSessionCallback = object : MediaSession.Callback() {
		override fun onPlay() {
			val audioFocusResult = audioManager.requestAudioFocus(
				audioFocusChangeListener,
				AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN
			)
			if (audioFocusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
				return
			}

			mediaSession.isActive = true
			mediaSession.setPlaybackState(
				stateBuilder.setState(
					PlaybackState.STATE_PLAYING,
					PlaybackState.PLAYBACK_POSITION_UNKNOWN, 1F
				).build()
			)
			player.prepare()
			player.playWhenReady = true
			Log.d("AudionauticaTag", "Player Ready")
		}

		override fun onPause() {
			Log.d("AudionauticaTag", "onPause")
			super.onPause()
			player.playWhenReady = false
			mediaSession.setPlaybackState(
				stateBuilder.setState(
					PlaybackState.STATE_PAUSED, PlaybackState.PLAYBACK_POSITION_UNKNOWN, 1F
				).build()
			)
		}


		override fun onStop() {
			Log.d("AudionauticaTag", "onStop")
			super.onStop()
			player.playWhenReady = false
			mediaSession.isActive = false

			mediaSession.setPlaybackState(
				stateBuilder.setState(
					PlaybackState.STATE_STOPPED, PlaybackState.PLAYBACK_POSITION_UNKNOWN, 1F
				).build()
			)

			audioManager.abandonAudioFocus(audioFocusChangeListener)
		}
	}

	override fun onCreate() {

		component.inject(this@MusicPlayerService)

		super.onCreate()

		player.addListener(
			object :Player.Listener {
				override fun onPlaybackStateChanged(playbackState: Int) {
					super.onPlaybackStateChanged(playbackState)
					when(playbackState) {
						ExoPlayer.STATE_ENDED -> {
							Log.d("AudionauticaTag1", "STATE_ENDED")
						}
						ExoPlayer.STATE_READY -> {
							Log.d("AudionauticaTag1", "STATE_READY")
						}
						ExoPlayer.STATE_IDLE -> {
							Log.d("AudionauticaTag1", "STATE_IDLE")
						}
					}

					if (playbackState == ExoPlayer.STATE_ENDED) {
						scope.launch {
							getPlayingEndedFlowUseCase().emit(Unit)
						}
					}
				}
			}
		)

		scope.launch(Dispatchers.Main) {
			getPlayingTrackFlowUseCase().collect {
				Log.d("AudionauticaTag1", "Player received new track: ${it.title}")
				if (it.id == -1) {
					if (nullTrack) {
						return@collect
					}
					else {
						nullTrack = true
					}
				}
				else {
					nullTrack = false
				}
				player.clearMediaItems()
				if (it.id != -1) {
					player.addMediaItem(MediaItem.fromUri(
						getTrackUriUseCase(it.id)
					))
				}
				mediaSessionCallback.onPlay()
				val metadata = metadataBuilder
					.putString(MediaMetadata.METADATA_KEY_TITLE, it.title)
					.putString(MediaMetadata.METADATA_KEY_ARTIST, it.artist)
					.putLong(CUSTOM_METADATA_KEY_ID, it.id.toLong())
					.putString(CUSTOM_METADATA_KEY_TAGS, it.tags.joinToString(CUSTOM_METADATA_TAGS_DELIMITER))
					.build()
				mediaSession.setMetadata(metadata)
			}
		}

		mediaSession = MediaSession(this@MusicPlayerService, "MusicPlayerService")
		mediaSession.setCallback(mediaSessionCallback)

		val activityIntent = Intent(applicationContext, MusicActivity::class.java)
		mediaSession.setSessionActivity(
			PendingIntent.getActivity(
				applicationContext,
				0,
				activityIntent,
				PendingIntent.FLAG_IMMUTABLE
			)
		)
	}

	private fun getNotification(title: String, text: String): Notification {
		val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		val notification: Notification
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val notificationChannel = NotificationChannel(
				CHANNEL_ID,
				CHANNEL_NAME,
				NotificationManager.IMPORTANCE_DEFAULT
			)
			notificationManager.createNotificationChannel(notificationChannel)
			notification = NotificationCompat.Builder(this, CHANNEL_ID)
				.setContentTitle(title)
				.setContentText(text)
				.setSmallIcon(R.drawable.ic_launcher_background)
				.build()
		} else {
			notification = NotificationCompat.Builder(this)
				.setContentTitle(title)
				.setContentText(text)
				.setSmallIcon(R.drawable.ic_launcher_background)
				.build()
		}
		return notification
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


		return START_STICKY
	}

	override fun onDestroy() {
		scope.cancel()
		mediaSession.release()
		player.release()
		super.onDestroy()
	}

	override fun onBind(p0: Intent?) = MusicPlayerServiceBinder(mediaSession)

	companion object {

		fun newIntent(connext: Context): Intent {
			return Intent(connext, MusicPlayerService::class.java)
		}

		val CUSTOM_METADATA_KEY_ID = "__id__"
		val CUSTOM_METADATA_KEY_TAGS = "__tags__"
		val CUSTOM_METADATA_TAGS_DELIMITER = "%"
	}

	class MusicPlayerServiceBinder(
		private val mediaSession: MediaSession
	): Binder() {
		fun getMediasessionToken() = mediaSession.sessionToken
	}


}