package com.kaelesty.audionautica.di

import android.app.Application
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.local.dbs.MusicDatabase
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
	fun provideTrackDao(db: MusicDatabase): TrackDao {
		return db.trackDao()
	}

	@Provides
	@ApplicationScope
	fun providePlaylistDao(db: MusicDatabase): PlaylistDao {
		return db.playlistDao()
	}

	@Provides
	@ApplicationScope
	fun provideJwtTool(application: Application) = JwtTool(application)
}