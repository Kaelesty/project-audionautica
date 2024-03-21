package com.kaelesty.audionautica.presentation.player

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Binder
import android.os.Bundle
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.kaelesty.audionautica.domain.player.PlayerQueueCommand
import com.kaelesty.audionautica.presentation.activities.MusicActivity
import com.kaelesty.audionautica.system.ModifiedApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerService : Service() {

	private val component by lazy {
		(application as ModifiedApplication).component
	}


	private val player by lazy {
		ExoPlayer
			.Builder(this@PlayerService)
			.build()
	}

	private val audioManager by lazy {
		getSystemService(Context.AUDIO_SERVICE) as AudioManager
	}

	val scope = CoroutineScope(Dispatchers.IO)
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

	@Inject
	lateinit var viewModel: PlayerViewModel

	private val audioFocusChangeListener: OnAudioFocusChangeListener =
		object : OnAudioFocusChangeListener {
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

		component.inject(this@PlayerService)

		viewModel.setPlayer(player)

		player.addListener(
			object: Player.Listener {
				override fun onMediaMetadataChanged(mediaMetadata: androidx.media3.common.MediaMetadata) {
					super.onMediaMetadataChanged(mediaMetadata)
					Log.d("PlayerTag", mediaMetadata.title.toString())
				}
			}
		)

		super.onCreate()

		scope.launch(Dispatchers.Main) {
			viewModel.getQueueFlow().collect {

				when (it) {
					is PlayerQueueCommand.AddToQueue -> {
						val metadata = androidx.media3.common.MediaMetadata.Builder()
							.setArtist(it.track.metadata.artist)
							.setTitle(it.track.metadata.title)
							.setExtras(Bundle().apply {
								putLong(CUSTOM_METADATA_KEY_ID, it.track.metadata.id.toLong())
								putString(
									CUSTOM_METADATA_KEY_TAGS, it.track.metadata.tags.joinToString(
										CUSTOM_METADATA_TAGS_DELIMITER
									)
								)
							})
							//.putString(MediaMetadata.METADATA_KEY_TITLE, it.track.metadata.title)
							//.putString(MediaMetadata.METADATA_KEY_ARTIST, it.track.metadata.artist)
							.build()
						//mediaSession.setMetadata(metadata)
						val mediaItem = MediaItem.Builder()
							.setMediaMetadata(metadata)
							.setUri(it.track.uri)
							.build()
						player.addMediaItem(mediaItem)
						mediaSessionCallback.onPlay()
					}

					is PlayerQueueCommand.DropQueue -> {
						player.clearMediaItems()
					}
				}
			}
		}

		mediaSession = MediaSession(this@PlayerService, "MusicPlayerService")
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
			return Intent(connext, PlayerService::class.java)
		}

		val CUSTOM_METADATA_KEY_ID = "__id__"
		val CUSTOM_METADATA_KEY_TAGS = "__tags__"
		val CUSTOM_METADATA_TAGS_DELIMITER = "%"
	}

	class MusicPlayerServiceBinder(
		private val mediaSession: MediaSession
	) : Binder() {
		fun getMediasessionToken() = mediaSession.sessionToken
	}


}