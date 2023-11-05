package com.kaelesty.audionautica.di

import android.content.Context
import com.kaelesty.audionautica.presentation.activities.AccessActivity
import com.kaelesty.audionautica.presentation.activities.MusicActivity
import com.kaelesty.audionautica.presentation.services.MusicPlayerService
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
	fun inject(service: MusicPlayerService)

	@Component.Factory
	interface ApplicationComponentFactory {

		fun create(
			@BindsInstance context: Context,
			@BindsInstance param: Int
		): ApplicationComponent
	}
}