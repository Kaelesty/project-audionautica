package com.kaelesty.audionautica.data.local.dbs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.local.dbmodels.TrackDbModel

@Database(
	entities = [TrackDbModel::class],
	version = 1,
	exportSchema = false,
)
abstract class TrackDatabase: RoomDatabase() {

	abstract fun dao(): TrackDao

	companion object {

		private var instance: TrackDatabase? = null
		private const val DB_NAME = "track_db"

		private val LOCK = Any()

		fun getInstance(context: Context): TrackDatabase {
			instance?.let {
				return it
			}
			synchronized(LOCK) {
				instance?.let {
					return it
				}
				val db = Room.databaseBuilder(
					context,
					TrackDatabase::class.java,
					DB_NAME
				).build()
				instance = db
				return db
			}
		}
	}

}