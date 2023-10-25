package com.kaelesty.audionautica.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaelesty.audionautica.domain.entities.Track

class MusicViewModel: ViewModel() {

	private val _tracks = MutableLiveData<List<Track>>()
	val tracks: LiveData<List<Track>> get() = _tracks

	fun search(query: String) {
		val tracksList = mutableListOf<Track>()
		repeat(15) {
			tracksList.add(Track(it))
		}
		_tracks.value = tracksList
	}
}