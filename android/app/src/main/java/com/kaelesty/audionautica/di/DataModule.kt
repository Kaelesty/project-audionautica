package com.kaelesty.audionautica.di

import android.app.Application
import android.content.Context
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.local.dbs.PlaylistDatabase
import com.kaelesty.audionautica.data.local.dbs.TrackDatabase
import com.kaelesty.audionautica.data.remote.api.AccessApiService
import com.kaelesty.audionautica.data.remote.api.ApiServiceFactory
import com.kaelesty.audionautica.data.remote.api.MusicApiService
import com.kaelesty.audionautica.data.repos.tools.JwtTool
import dagger.Module
import dagger.Provides

@Module
class DataModule {

	@Provides
	@ApplicationScope
	fun provideAccessApiService(): AccessApiService {
		return ApiServiceFactory.accessService
	}

	@Provides
	@ApplicationScope
	fun provideMusicApiService(): MusicApiService {
		return ApiServiceFactory.musicService
	}

	@Provides
	@ApplicationScope
	fun provideTrackDao(db: TrackDatabase): TrackDao {
		return db.dao()
	}

	@Provides
	@ApplicationScope
	fun providePlaylistDao(db: PlaylistDatabase): PlaylistDao {
		return db.dao()
	}

	@Provides
	@ApplicationScope
	fun provideJwtTool(application: Application) = JwtTool(application)
}