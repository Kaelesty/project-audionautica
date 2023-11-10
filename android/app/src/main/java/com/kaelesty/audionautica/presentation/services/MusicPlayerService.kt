package com.kaelesty.audionautica.presentation.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import com.kaelesty.audionautica.R
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.activities.MusicActivity
import com.kaelesty.audionautica.system.ModifiedApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import java.io.File

class MusicPlayerService : Service() {

	private val component by lazy {
		(application as ModifiedApplication).component
	}

	private val player by lazy {
		androidx.media3.exoplayer.ExoPlayer
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
			Log.d("MusicService", "onPlay")
			val audioFocusResult = audioManager.requestAudioFocus(
				audioFocusChangeListener,
				AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN
			)
			if (audioFocusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
				return
			}
			// SAMPLE TODO REMOVE
			val track = Track(
				artist = "re:Tye",
				id = 0,
				duration = 42422,
				title = "Akeboshi",
				musicFile = Uri.fromFile(File(
					applicationContext.filesDir.absolutePath + "/1234.mp3"
				)),
				posterFile = Uri.fromFile(
					File(
						applicationContext.filesDir.absolutePath + "/1234.mp3"
					)
				)
			)
			//
			val metadata = metadataBuilder
				.putBitmap(
					MediaMetadata.METADATA_KEY_ART,
					BitmapFactory.decodeResource(resources, R.drawable.example_track_poster_2)
				)
				.putString(MediaMetadata.METADATA_KEY_TITLE, track.title)
				.putString(MediaMetadata.METADATA_KEY_ARTIST, track.artist)
				.build()
			mediaSession.setMetadata(metadata)

			mediaSession.isActive = true
			mediaSession.setPlaybackState(
				stateBuilder.setState(
					PlaybackState.STATE_PLAYING,
					PlaybackState.PLAYBACK_POSITION_UNKNOWN, 1F
				).build()
			)
			player.addMediaItem(
				MediaItem.fromUri(
					track.musicFile
				)
			)
			player.prepare()
			player.playWhenReady = true
		}

		override fun onPause() {
			Log.d("MusicService", "onPause")
			super.onPause()
			player.playWhenReady = false
			mediaSession.setPlaybackState(
				stateBuilder.setState(
					PlaybackState.STATE_PAUSED, PlaybackState.PLAYBACK_POSITION_UNKNOWN, 1F
				).build()
			)
		}


		override fun onStop() {
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
		Log.e("MusicService", "onCreate")
		//startForeground(22, getNotification("Foreground service", "in progress..."))

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

		const val TAG = "MyService - Foreground"

		fun newIntent(connext: Context): Intent {
			return Intent(connext, MusicPlayerService::class.java)
		}
	}

	class MusicPlayerServiceBinder(
		private val mediaSession: MediaSession
	): Binder() {
		fun getMediasessionToken() = mediaSession.sessionToken
	}


}