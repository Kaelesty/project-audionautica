package com.kaelesty.audionautica.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaelesty.audionautica.data.local.dbmodels.PlaylistCacheDbModel

@Dao
interface PlaylistDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun updatePlaylist(playlist: PlaylistCacheDbModel)

	@Query("SELECT * FROM `playlists`")
	fun getAll(): LiveData<List<PlaylistCacheDbModel>>

	@Query("DELETE FROM playlists")
	fun nukeTable()

	@Query("SELECT * FROM `playlists` WHERE id = :id")
	fun getLivePlaylist(id: Int): LiveData<PlaylistCacheDbModel>

	@Query("SELECT * FROM `playlists` WHERE id = :id")
	fun getPlaylist(id: Int): PlaylistCacheDbModel
}