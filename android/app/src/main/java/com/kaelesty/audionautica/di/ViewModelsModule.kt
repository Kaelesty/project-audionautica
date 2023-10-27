package com.kaelesty.audionautica.di

import androidx.lifecycle.ViewModel
import com.kaelesty.audionautica.presentation.viewmodels.AccessViewModel
import com.kaelesty.audionautica.presentation.viewmodels.MusicViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
interface ViewModelsModule {

	@IntoMap
	@StringKey("AccessViewModel")
	@Binds
	fun bindAccessViewModel(impl: AccessViewModel): ViewModel

	@IntoMap
	@StringKey("MusicViewModel")
	@Binds
	fun bindsMusicViewModel(impl: MusicViewModel): ViewModel
}