package com.kaelesty.audionautica.di

import com.kaelesty.audionautica.data.repos.AccessRepo
import com.kaelesty.audionautica.data.repos.MusicRepo
import com.kaelesty.audionautica.domain.repos.IAccessRepo
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import dagger.Binds
import dagger.Module

@Module
abstract class DomainModule {

	@Binds
	abstract fun provideAccessRepo(impl: AccessRepo): IAccessRepo

	@Binds
	abstract fun provideMusicRepo(impl: MusicRepo): IMusicRepo
}