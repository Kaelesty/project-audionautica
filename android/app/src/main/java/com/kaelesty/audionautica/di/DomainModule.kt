package com.kaelesty.audionautica.di

import com.kaelesty.audionautica.data.localplaylists.LocalPlaylistsRepo
import com.kaelesty.audionautica.data.player.PlayerController
import com.kaelesty.audionautica.data.playlists.PlaylistsRepo
import com.kaelesty.audionautica.data.reco.RecoRepository
import com.kaelesty.audionautica.data.repos.AccessRepo
import com.kaelesty.audionautica.data.repos.MusicRepo
import com.kaelesty.audionautica.domain.localplaylists.ILocalPlaylistsRepo
import com.kaelesty.audionautica.domain.player.IPlayerController
import com.kaelesty.audionautica.domain.playlists.IPlaylistsRepo
import com.kaelesty.audionautica.domain.reco.IRecoRepository
import com.kaelesty.audionautica.domain.repos.IAccessRepo
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import dagger.Binds
import dagger.Module

@Module
abstract class DomainModule {

	@Binds
	@ApplicationScope
	abstract fun provideAccessRepo(impl: AccessRepo): IAccessRepo

	@Binds
	@ApplicationScope
	abstract fun provideMusicRepo(impl: MusicRepo): IMusicRepo

	@Binds
	@ApplicationScope
	abstract fun providePlaylistsRepo(impl: PlaylistsRepo): IPlaylistsRepo

	@Binds
	@ApplicationScope
	abstract fun provideRecoRepo(impl: RecoRepository): IRecoRepository

	@Binds
	@ApplicationScope
	abstract fun providePqc(impl: PlayerController): IPlayerController

	@Binds
	@ApplicationScope
	abstract fun provideLocalPlaylistsRepo(impl: LocalPlaylistsRepo): ILocalPlaylistsRepo
}