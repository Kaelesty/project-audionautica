package com.kaelesty.audionautica.di

import android.content.Context
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.local.dbs.PlaylistDatabase
import com.kaelesty.audionautica.data.local.dbs.TrackDatabase
import com.kaelesty.audionautica.data.remote.api.AccessApiService
import com.kaelesty.audionautica.data.remote.api.ApiServiceFactory
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
	fun provideTrackDao(context: Context): TrackDao {
		return TrackDatabase.getInstance(context).dao()
	}

	@Provides
	@ApplicationScope
	fun providePlaylistDao(context: Context): PlaylistDao {
		return PlaylistDatabase.getInstance(context).dao()
	}
}