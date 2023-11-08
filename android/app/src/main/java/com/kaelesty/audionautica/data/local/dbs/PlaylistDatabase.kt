package com.kaelesty.audionautica.data.local.dbs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.dbmodels.PlaylistDbModel
import com.kaelesty.audionautica.domain.entities.Track

@Database(
	entities = [PlaylistDbModel::class],
	version = 1,
	exportSchema = false,
)
abstract class PlaylistDatabase() : RoomDatabase() {

	abstract fun dao(): PlaylistDao

	companion object {

		private var instance: PlaylistDatabase? = null
		private const val DB_NAME = "coin_db"

		private val LOCK = Any()

		fun getInstance(context: Context): PlaylistDatabase {
			instance?.let {
				return it
			}
			synchronized(LOCK) {
				instance?.let {
					return it
				}
				val db = Room.databaseBuilder(
					context,
					PlaylistDatabase::class.java,
					DB_NAME
				).build()
				checkFavoritesCreated(db)

				instance = db

				return db
			}
		}

		private fun checkFavoritesCreated(db: PlaylistDatabase) {
			/*
			Adding tracks to favorites is done through a special playlist "Favorites",
			which is not displayed in the general list of playlists.
			It must always exist, therefore, every time we create a database instance,
			we check whether it has been created.
			 */
			with(db.dao()) {
				if (getAll().isEmpty()) {
					createPlaylist(
						PlaylistDbModel(
							id = 0,
							posterFile = null,
							title = "Favorites",
						)
					)
				}
			}
		}
	}
}