package com.kaelesty.audionautica.presentation.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.kaelesty.audionautica.domain.player.PlayerQueueCommand
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.system.ModifiedApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerService : MediaSessionService() {

	private val component by lazy {
		(application as ModifiedApplication).component
	}

	private var currentPlaylist: UserPlaylist.Entire? = null


	private val player by lazy {
		ExoPlayer
			.Builder(this@PlayerService)
			.build()
	}

	val scope = CoroutineScope(Dispatchers.IO)

	private lateinit var mediaSession: MediaSession

	@Inject
	lateinit var viewModel: PlayerViewModel



	@OptIn(UnstableApi::class) override fun onCreate() {

		component.inject(this@PlayerService)

		viewModel.setPlayer(player)

		super.onCreate()

		val forwardButton = CommandButton.Builder()
			.setDisplayName("Forward")
			.setIconResId(android.R.drawable.ic_media_next)
			.setSessionCommand(SessionCommand(CUSTOM_COMMAND_FORWARD, Bundle()))
			.build()

		mediaSession = MediaSession.Builder(this, player)
			.setCustomLayout(ImmutableList.of(forwardButton))
			.setCallback(CustomMediaSessionCallback())
			.build()

		scope.launch(Dispatchers.Main) {
			viewModel.commandFlow.collect {
				when (it) {
					is PlayerQueueCommand.Play -> {
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
								putLong(
									CUSTOM_METADATA_KEY_PLAYLIST_ID,
									(currentPlaylist?.id ?: - 1).toLong()
								)
							})
							.build()
						val mediaItem = MediaItem.Builder()
							.setMediaMetadata(metadata)
							.setUri(it.track.uri)
							.build()
						player.addMediaItem(mediaItem)
						player.prepare()
						player.play()
						Log.d("PlayerTag", "PlayerService.onCommand: PLAYING")
					}

					is PlayerQueueCommand.Drop -> {
						player.clearMediaItems()
						currentPlaylist = null
					}
				}
			}
		}
	}


	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		super.onStartCommand(intent, flags, startId)
		return START_STICKY
	}

	override fun onDestroy() {
		scope.cancel()
		mediaSession.release()
		player.release()
		super.onDestroy()
	}

	override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
		return mediaSession
	}

	companion object {

		fun newIntent(connext: Context): Intent {
			return Intent(connext, PlayerService::class.java)
		}

		val CUSTOM_METADATA_KEY_ID = "__id__"
		val CUSTOM_METADATA_KEY_PLAYLIST_ID = "__id__"
		val CUSTOM_METADATA_KEY_TAGS = "__tags__"
		val CUSTOM_METADATA_TAGS_DELIMITER = "%"

		val CUSTOM_COMMAND_FORWARD = "forward__"
	}

	private inner class CustomMediaSessionCallback: MediaSession.Callback {
		@OptIn(UnstableApi::class) override fun onConnect(
			session: MediaSession,
			controller: MediaSession.ControllerInfo
		): MediaSession.ConnectionResult {
			val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
				.add(SessionCommand(CUSTOM_COMMAND_FORWARD, Bundle.EMPTY))
				.build()
			return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
				.setAvailableSessionCommands(sessionCommands)
				.build()
		}

		override fun onCustomCommand(
			session: MediaSession,
			controller: MediaSession.ControllerInfo,
			customCommand: SessionCommand,
			args: Bundle
		): ListenableFuture<SessionResult> {
			if (customCommand.customAction == CUSTOM_COMMAND_FORWARD) {
				session.player.clearMediaItems()
				return Futures.immediateFuture(
					SessionResult(SessionResult.RESULT_SUCCESS)
				)
			}
			return Futures.immediateFuture(
				SessionResult(SessionResult.RESULT_SUCCESS)
			)
		}
	}

}