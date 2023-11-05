package com.kaelesty.audionautica.di

import android.media.MediaPlayer
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

	@Provides
	@ApplicationScope
	fun provideMediaPlayer() = MediaPlayer()
}