package com.kaelesty.audionautica.system

import android.app.Application
import com.kaelesty.audionautica.di.DaggerApplicationComponent

class ModifiedApplication: Application() {

	val component by lazy {
		DaggerApplicationComponent.factory()
			.create(this@ModifiedApplication, 123)
	}
}