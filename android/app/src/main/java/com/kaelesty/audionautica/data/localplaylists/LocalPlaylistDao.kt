package com.kaelesty.audionautica.data.localplaylists

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaelesty.audionautica.data.localplaylists.LocalPlaylistDbModel

@Dao
interface LocalPlaylistDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun updatePlaylist(playlist: LocalPlaylistDbModel)

	@Query("SELECT * FROM `local-playlists`")
	fun getAll(): LiveData<List<LocalPlaylistDbModel>>

	@Query("DELETE FROM `local-playlists` WHERE id = :id")
	fun deletePlaylist(id: Int)
}