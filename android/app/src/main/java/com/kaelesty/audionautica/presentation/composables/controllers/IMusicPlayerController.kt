package com.kaelesty.audionautica.presentation.composables.controllers

interface IMusicPlayerController {

	fun onPlay() // Play Button >

	fun onPause() // Pause Button ||

	fun onNext() // Skip to Next Track >|

	fun onPrevious() // Skip to Prev Track |<

	fun onRepeat() // Toggle Repeat Mode On/Off

	fun onLike() // Toggle Track Liked State
}