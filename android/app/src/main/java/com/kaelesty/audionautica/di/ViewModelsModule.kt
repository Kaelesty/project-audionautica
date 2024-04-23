package com.kaelesty.audionautica.di

import androidx.lifecycle.ViewModel
import com.kaelesty.audionautica.presentation.player.PlayerViewModel
import com.kaelesty.audionautica.presentation.playlists.PlaylistsViewModel
import com.kaelesty.audionautica.presentation.playlists.upload.PlaylistUploadViewModel
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

	@IntoMap
	@StringKey("PlayerViewModel")
	@Binds
	fun bindsPlayerViewModel(impl: PlayerViewModel): ViewModel

	@IntoMap
	@StringKey("PlaylistsViewModel")
	@Binds
	fun bindsPlaylistsViewModel(impl: PlaylistsViewModel): ViewModel

	@IntoMap
	@StringKey("PlaylistUploadViewModel")
	@Binds
	fun bindsPlaylistUploadViewModel(impl: PlaylistUploadViewModel): ViewModel

}