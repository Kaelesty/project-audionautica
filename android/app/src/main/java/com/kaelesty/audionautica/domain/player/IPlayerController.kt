package com.kaelesty.audionautica.domain.player

import androidx.media3.common.Player
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.playlists.UserPlaylist
import com.kaelesty.audionautica.presentation.player.PlayerViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IPlayerController {

	suspend fun setQueue(tracks: List<Track>, startPosition: Int = 0)

	suspend fun setQueueAsPlaylist(playlist: UserPlaylist.Entire, tracks: List<Track>, startPosition: Int = 0)

	fun getCommandFlow(): SharedFlow<PlayerQueueCommand>

	suspend fun dropQueue()

	suspend fun setPlayer(player: Player)

	fun playNext()

	fun playPrev()

	fun setShuffle(state: Boolean)

	fun setRepeat(state: Repeat)

	fun play()

	fun pause()

	fun getStateFlow(): StateFlow<PlayerState>

	fun seekTo(progress: Long)

	fun playFromPosition(pos: Int)

	fun setPlaybackSpeed(speed: Float)
}

sealed class Repeat {
	object False: Repeat()
	object True: Repeat()
	object One: Repeat()

}

fun Repeat.toInt() = when (this) {
	is Repeat.False -> 0
	is Repeat.True -> 1
	is Repeat.One -> 2
}

fun Int.toRepeatMode() = when(this) {
	0 -> Repeat.False
	1 -> Repeat.True
	2 -> Repeat.One
	else -> throw Exception("Could not convert $this to RepeatMode")
}

data class PlayerState(
	val meta: Track?,
	val playing: Boolean,
	val progress: Long,
	val duration: Long,
	val playlistId: Int? = null,
	val shuffle: Boolean,
	val repeat: Repeat,
	val queue: List<Track>? = listOf(),
	val currentQueuePosition: Int = -1,
)