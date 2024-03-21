package com.kaelesty.audionautica.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaelesty.audionautica.data.local.dbmodels.TokenDbModel

@Dao
interface TokenDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun saveToken(tokenDbModel: TokenDbModel)

	@Query("DELETE from token WHERE id = 0")
	fun dropToken()

	@Query("SELECT * from token WHERE id = 0")
	fun getToken(): TokenDbModel
}