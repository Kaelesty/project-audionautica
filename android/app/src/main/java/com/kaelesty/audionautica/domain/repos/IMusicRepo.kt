package com.kaelesty.audionautica.domain.repos

import android.net.Uri
import androidx.lifecycle.LiveData
import com.kaelesty.audionautica.domain.entities.Playlist
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackExp
import com.kaelesty.audionautica.domain.entities.TracksToPlay
import com.kaelesty.audionautica.domain.returncodes.UploadTrackRC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface IMusicRepo {

	fun getTracks(): LiveData<List<Track>>

	fun getPlaylists(): LiveData<List<Playlist>>

	suspend fun addTrack(track: TrackExp)

	suspend fun uploadTrack(track: Track, uri: Uri): UploadTrackRC

	suspend fun search(query: String): List<Track>

	suspend fun addToTracksQueue(
		track: List<Track>,
	)

	suspend fun getTrackUri(id: Int): Uri

	fun getTracksQueueFlow(): SharedFlow<TracksToPlay>

	suspend fun addTrackToPlaylist(track: Track, playlistId: Int)

	suspend fun removeTrackFromPlaylist(track: Track, playlistId: Int)

	fun getPlaylistTracks(id: Int): List<Track>

	suspend fun createPlaylist(playlist: Playlist)

	suspend fun deletePlaylist(playlistId: Int)

	fun getPlayingTrackFlow(): SharedFlow<Track>

	fun getPlayingEndedFlow(): MutableSharedFlow<Unit>

	suspend fun saveTrack(track: Track)

	suspend fun deleteTrack(track: Track)

	suspend fun playNext()

	suspend fun playPrev()
}