package com.kaelesty.audionautica.di

import android.app.Application
import android.content.ContentResolver
import com.kaelesty.audionautica.data.local.dbs.MusicDatabase
import com.kaelesty.audionautica.presentation.activities.AccessActivity
import com.kaelesty.audionautica.presentation.activities.AddTrackActivity
import com.kaelesty.audionautica.presentation.activities.MusicActivity
import com.kaelesty.audionautica.presentation.player.PlayerService
import dagger.BindsInstance
import dagger.Component

@Component(
	modules = [
		DomainModule::class,
		DataModule::class,
		ViewModelsModule::class,
		PresentationModule::class,
	]
)
@ApplicationScope
interface ApplicationComponent {

	fun inject(activity: AccessActivity)
	fun inject(activity: MusicActivity)
	fun inject(service: PlayerService)
	fun inject(activity: AddTrackActivity)

	@Component.Factory
	interface ApplicationComponentFactory {

		fun create(
			@BindsInstance application: Application,
			@BindsInstance contentResolver: ContentResolver,
			@BindsInstance musicDb: MusicDatabase
		): ApplicationComponent
	}
}