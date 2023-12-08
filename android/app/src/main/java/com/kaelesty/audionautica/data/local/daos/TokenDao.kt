package com.kaelesty.audionautica.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaelesty.audionautica.data.local.dbmodels.PlaylistDbModel
import com.kaelesty.audionautica.data.local.dbmodels.TokenDbModel
import com.kaelesty.audionautica.domain.entities.Playlist

@Dao
interface TokenDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun saveToken(tokenDbModel: TokenDbModel)

	@Query("DELETE from token WHERE id = 0")
	fun dropToken()

	@Query("SELECT * from token WHERE id = 0")
	fun getToken(): TokenDbModel
}