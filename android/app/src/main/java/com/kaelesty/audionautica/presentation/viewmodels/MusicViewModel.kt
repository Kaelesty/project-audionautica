package com.kaelesty.audionautica.presentation.viewmodels

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaelesty.audionautica.data.remote.api.ApiServiceFactory
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.entities.TrackSample
import com.kaelesty.audionautica.domain.usecases.CreateTrackUseCase
import com.kaelesty.audionautica.domain.usecases.GetAllTracksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class MusicViewModel @Inject constructor(
	private val context: Context,
	private val getAllTracksUseCase: GetAllTracksUseCase,
	private val createTrackUseCase: CreateTrackUseCase,
): ViewModel() {

	private val FILE_PATH = "/Audionautica"

	// ON SEARCH SCREEN
	private val _tracks = MutableLiveData<List<TrackSample>>()
	val tracks: LiveData<List<TrackSample>> get() = _tracks

	// ON FAVORITES SCREEN
	val favorites: LiveData<List<Track>> = getAllTracksUseCase()

	fun addTrack(
		artist: String,
		title: String,
		posterFile: Uri,
		musicFile: Uri,
	) {
		val track = Track(
			artist = artist,
			duration = 0,
			id = -1,
			musicFile = musicFile,
			posterFile = posterFile,
			title = title
		)

		viewModelScope.launch(Dispatchers.IO) {
			createTrackUseCase(track)
		}
	}

	fun search(query: String) {
		val tracksList = mutableListOf<TrackSample>()
		repeat(15) {
			tracksList.add(TrackSample(it))
		}
		_tracks.value = tracksList
	}

	fun downloadTrack() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val request = ApiServiceFactory.musicService.downloadTrackSample()
				Log.e("MusicViewModel", request.code().toString())
				val responseBody = request.body()
				responseBody?.let {
					saveFile(it)
				}
			}
			catch (e: Exception) {
				Log.e("MusicViewModel", e.toString())
			}
		}
	}

	private fun saveFile(body: ResponseBody) {
		var input: InputStream? = null
		try {
			input = body.byteStream()

			val fileName="/1234.mp3"
			val pathWhereYouWantToSaveFile = context.filesDir.absolutePath+fileName
			Log.e("MusicViewModel", context.filesDir.absolutePath)
			val fos = FileOutputStream(pathWhereYouWantToSaveFile)
			fos.use {output ->
				val buffer = ByteArray(4 * 1024)
				var read: Int
				while(input.read(buffer).also { read = it } != -1) {
					output.write(buffer, 0, read)
				}

				output.flush()

			}
		}
		catch (exception: Exception) {
			Log.e("MusicViewModel", exception.toString())
		}
		finally {
			input?.close()
		}
	}

}