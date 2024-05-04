package com.kaelesty.audionautica.di

import androidx.lifecycle.ViewModel
import com.kaelesty.audionautica.presentation.player.PlayerViewModel
import com.kaelesty.audionautica.presentation.playlists.addtracktoplaylist.AddTrackToPlaylistViewModel
import com.kaelesty.audionautica.presentation.playlists.list.PlaylistsViewModel
import com.kaelesty.audionautica.presentation.playlists.details.PlaylistDetailsViewModel
import com.kaelesty.audionautica.presentation.playlists.upload.PlaylistUploadViewModel
import com.kaelesty.audionautica.presentation.search.SearchViewModel
import com.kaelesty.audionautica.presentation.settings.SettingsViewModel
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
	@ApplicationScope
	fun bindAccessViewModel(impl: AccessViewModel): ViewModel

	@IntoMap
	@StringKey("MusicViewModel")
	@Binds
	@ApplicationScope
	fun bindsMusicViewModel(impl: MusicViewModel): ViewModel

	@IntoMap
	@StringKey("PlayerViewModel")
	@Binds
	@ApplicationScope
	fun bindsPlayerViewModel(impl: PlayerViewModel): ViewModel

	@IntoMap
	@StringKey("PlaylistsViewModel")
	@Binds
	@ApplicationScope
	fun bindsPlaylistsViewModel(impl: PlaylistsViewModel): ViewModel

	@IntoMap
	@StringKey("PlaylistUploadViewModel")
	@Binds
	@ApplicationScope
	fun bindsPlaylistUploadViewModel(impl: PlaylistUploadViewModel): ViewModel

	@IntoMap
	@StringKey("PlaylistDetailsViewModel")
	@Binds
	@ApplicationScope
	fun bindsPlaylistDetailsViewModel(impl: PlaylistDetailsViewModel): ViewModel

	@IntoMap
	@StringKey("SettingsViewModel")
	@Binds
	@ApplicationScope
	fun bindsSettingsViewModel(impl: SettingsViewModel): ViewModel

	@IntoMap
	@StringKey("AddTrackToPlaylistViewModel")
	@Binds
	@ApplicationScope
	fun bindsAddTrackToPlaylistViewModel(impl: AddTrackToPlaylistViewModel): ViewModel

	@IntoMap
	@StringKey("SearchViewModel")
	@Binds
	@ApplicationScope
	fun bindsSearchViewModel(impl: SearchViewModel): ViewModel
}