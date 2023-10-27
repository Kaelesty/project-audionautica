package com.kaelesty.audionautica.di

import android.content.Context
import com.kaelesty.audionautica.presentation.activities.AccessActivity
import com.kaelesty.audionautica.presentation.activities.MusicActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Inject

@Component(
	modules = [
		DomainModule::class,
		DataModule::class,
		ViewModelsModule::class,
	]
)
@ApplicationScope
interface ApplicationComponent {

	fun inject(activity: AccessActivity)
	fun inject(activity: MusicActivity)

	@Component.Factory
	interface ApplicationComponentFactory {

		fun create(
			@BindsInstance context: Context,
			@BindsInstance param: Int
		): ApplicationComponent
	}
}