package com.kaelesty.audionautica.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaelesty.audionautica.data.local.dbmodels.PlaylistDbModel
import com.kaelesty.audionautica.domain.entities.Playlist

@Dao
interface PlaylistDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun createPlaylist(playlist: PlaylistDbModel)

	@Query("SELECT * FROM playlists")
	fun getAll(): List<PlaylistDbModel>
}