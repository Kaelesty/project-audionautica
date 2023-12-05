package com.kaelesty.audionautica.data.local.daos

import androidx.lifecycle.LiveData
import androidx.media3.extractor.mp4.Track
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaelesty.audionautica.data.local.dbmodels.TrackDbModel
import com.kaelesty.audionautica.domain.entities.Playlist
import kotlinx.coroutines.flow.SharedFlow
import java.util.concurrent.Flow

@Dao
interface TrackDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun createTrack(track: TrackDbModel)

	@Query("SELECT * FROM `tracks-`")
	fun getAll(): LiveData<List<TrackDbModel>>

	@Query("SELECT * FROM `tracks-` WHERE id IN (:ids)")
	fun getById(ids: List<Int>): List<TrackDbModel>
}