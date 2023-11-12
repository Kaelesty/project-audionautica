package com.kaelesty.audionautica.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaelesty.audionautica.data.local.dbmodels.TrackDbModel

@Dao
interface TrackDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun createTrack(track: TrackDbModel)

	@Query("SELECT * FROM `tracks-`")
	fun getAll(): LiveData<List<TrackDbModel>>
}