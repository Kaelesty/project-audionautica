package com.kaelesty.audionautica.system

import android.app.Application
import androidx.room.Room
import com.kaelesty.audionautica.data.local.dbs.PlaylistDatabase
import com.kaelesty.audionautica.data.local.dbs.TrackDatabase
import com.kaelesty.audionautica.di.DaggerApplicationComponent

class ModifiedApplication: Application() {

	val component by lazy {
		DaggerApplicationComponent.factory()
			.create(
				this@ModifiedApplication,
				contentResolver,
				Room.databaseBuilder(this, TrackDatabase::class.java, "trackdb").build(),
				Room.databaseBuilder(this, PlaylistDatabase::class.java, "playlistdb").build()
			)
	}
}