package com.kaelesty.audionautica.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaelesty.audionautica.data.local.dbmodels.PlaylistDbModel

@Dao
interface PlaylistDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun updatePlaylist(playlist: PlaylistDbModel)

	@Query("SELECT * FROM `playlists`")
	fun getAll(): LiveData<List<PlaylistDbModel>>

	@Query("DELETE FROM playlists")
	fun nukeTable()

	@Query("SELECT * FROM `playlists` WHERE id = :id")
	fun getLivePlaylist(id: Int): LiveData<PlaylistDbModel>
}