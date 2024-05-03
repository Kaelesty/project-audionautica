package com.kaelesty.audionautica.data.local.dbs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.daos.AccessDao
import com.kaelesty.audionautica.data.local.daos.LocalPlaylistDao
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.local.dbmodels.LocalPlaylistDbModel
import com.kaelesty.audionautica.data.local.dbmodels.LoginDbModel
import com.kaelesty.audionautica.data.local.dbmodels.PlaylistDbModel
import com.kaelesty.audionautica.data.local.dbmodels.TokenDbModel
import com.kaelesty.audionautica.data.local.dbmodels.TrackDbModel
import com.kaelesty.audionautica.data.local.typeconverters.ListIntConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Database(
	entities = [
		PlaylistDbModel::class,
		TrackDbModel::class,
		TokenDbModel::class,
		LoginDbModel::class,
		LocalPlaylistDbModel::class,
	],
	version = 1,
	exportSchema = false,
)
@TypeConverters(ListIntConverter::class)
abstract class MusicDatabase() : RoomDatabase() {

	abstract fun playlistDao(): PlaylistDao
	abstract fun trackDao(): TrackDao
	abstract fun tokenDao(): AccessDao
	abstract fun localPlaylistDao(): LocalPlaylistDao

	companion object {

		private var instance: MusicDatabase? = null
		private const val DB_NAME = "music_db"

		private val LOCK = Any()

		fun getInstance(context: Context): MusicDatabase {
			instance?.let {
				return it
			}
			synchronized(LOCK) {
				instance?.let {
					return it
				}
				val db = Room.databaseBuilder(
					context,
					MusicDatabase::class.java,
					DB_NAME
				).build()
				//checkFavoritesCreated(db)

				instance = db

				return db
			}
		}
	}
}