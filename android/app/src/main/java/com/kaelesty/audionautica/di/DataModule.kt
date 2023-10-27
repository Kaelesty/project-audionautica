package com.kaelesty.audionautica.di

import android.content.Context
import com.kaelesty.audionautica.data.remote.api.AccessApiService
import com.kaelesty.audionautica.data.remote.api.AccessServiceFactory
import com.kaelesty.audionautica.data.repos.AccessRepo
import com.kaelesty.audionautica.data.repos.MusicRepo
import com.kaelesty.audionautica.domain.repos.IAccessRepo
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class DataModule {

	@Provides
	@ApplicationScope
	fun provideAccessApiService(): AccessApiService {
		return AccessServiceFactory.apiService
	}
}