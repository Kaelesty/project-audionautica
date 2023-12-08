package com.kaelesty.audionautica.system

import android.app.Application
import androidx.room.Room
import com.kaelesty.audionautica.data.local.dbs.MusicDatabase
import com.kaelesty.audionautica.di.DaggerApplicationComponent

class ModifiedApplication: Application() {

	val component by lazy {
		DaggerApplicationComponent.factory()
			.create(
				this@ModifiedApplication,
				contentResolver,
				Room.databaseBuilder(this, MusicDatabase::class.java, "playlistdb")
					.allowMainThreadQueries()
					.build()
			)
	}
}