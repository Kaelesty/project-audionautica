package com.kaelesty.audionautica.data.local.dbs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.daos.TrackDao
import com.kaelesty.audionautica.data.local.dbmodels.PlaylistDbModel
import com.kaelesty.audionautica.data.local.dbmodels.TrackDbModel
import com.kaelesty.audionautica.data.local.typeconverters.ListIntConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
	entities = [PlaylistDbModel::class, TrackDbModel::class],
	version = 1,
	exportSchema = false,
)
@TypeConverters(ListIntConverter::class)
abstract class MusicDatabase() : RoomDatabase() {

	abstract fun playlistDao(): PlaylistDao
	abstract fun trackDao(): TrackDao

	private val dbSetupScope = CoroutineScope(Dispatchers.IO)

	fun checkFavoritesCreated() {
		/*
		Adding tracks to favorites is done through a special playlist "Favorites",
		which is not displayed in the general list of playlists.
		It must always exist, therefore, every time we create a database instance,
		we check whether it has been created.
		 */
		with(this.playlistDao()) {
			val value = getAll().value
			if (value.isNullOrEmpty()) {
				dbSetupScope.launch {
					createPlaylistWithoutReplacing(
						PlaylistDbModel(
							id = null,
							title = "Favorites",
							trackIds = listOf(),
						)
					)
				}
			}
		}
	}

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