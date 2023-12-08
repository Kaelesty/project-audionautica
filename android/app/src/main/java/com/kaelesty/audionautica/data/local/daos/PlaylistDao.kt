package com.kaelesty.audionautica.data.local.daos

import androidx.lifecycle.LiveData
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

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun createPlaylistWithoutReplacing(playlist: PlaylistDbModel)

	@Query("SELECT * FROM `playlists-`")
	fun getAll(): LiveData<List<PlaylistDbModel>>

	@Query("SELECT * from `playlists-` WHERE id= :playlistId")
	fun getPlaylist(playlistId: Int): PlaylistDbModel

	@Query("DELETE from `playlists-` WHERE id= :playlistId")
	fun deletePlaylist(playlistId: Int)
}