package com.kaelesty.audionautica.presentation

import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.presentation.player.PlayerService

fun MediaController.getCurrentTrack(): Track? {
	val meta = metadata ?: return null
	return Track(
		id = meta.getLong(PlayerService.CUSTOM_METADATA_KEY_ID).toInt(),
		artist = meta.getString(MediaMetadata.METADATA_KEY_ARTIST),
		title = meta.getString(MediaMetadata.METADATA_KEY_TITLE),
		tags = meta.getString(PlayerService.CUSTOM_METADATA_KEY_TAGS)
			.split(PlayerService.CUSTOM_METADATA_TAGS_DELIMITER)
	)
}

fun MediaController.isPlaying(): Boolean = playbackState?.state == PlaybackState.STATE_PLAYING

fun androidx.media3.common.MediaMetadata.getCurrentPlaylistId(): Int {
	return (extras?.getLong(PlayerService.CUSTOM_METADATA_KEY_PLAYLIST_ID) ?: -1).toInt()
}

fun MediaMetadata.getCurrentTrack(): Track? {
	return Track(
		artist = getString(MediaMetadata.METADATA_KEY_ARTIST) ?: return null,
		id = getLong(PlayerService.CUSTOM_METADATA_KEY_ID)?.toInt() ?: return null,
		title = getString(MediaMetadata.METADATA_KEY_TITLE) ?: return null,
		tags = getString(PlayerService.CUSTOM_METADATA_KEY_TAGS)
			?.split(PlayerService.CUSTOM_METADATA_TAGS_DELIMITER) ?: return null
	)
}

fun androidx.media3.common.MediaMetadata.getCurrentTrack(): Track? {
	return Track(
		artist = artist.toString()?: return null,
		title = title.toString() ?: return null,
		id = extras?.getLong(PlayerService.CUSTOM_METADATA_KEY_ID)?.toInt()?: return null,
		tags = extras?.getString(PlayerService.CUSTOM_METADATA_KEY_TAGS)
			?.split(PlayerService.CUSTOM_METADATA_TAGS_DELIMITER) ?: return null
	)
}