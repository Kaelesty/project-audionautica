package com.kaelesty.audionautica.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaelesty.audionautica.data.local.dbmodels.LoginDbModel
import com.kaelesty.audionautica.data.local.dbmodels.TokenDbModel

@Dao
interface AccessDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun saveToken(tokenDbModel: TokenDbModel)

	@Query("DELETE from token WHERE id = 0")
	fun dropToken()

	@Query("SELECT * from token WHERE id = 0")
	fun getToken(): TokenDbModel

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun saveLogin(dbModel: LoginDbModel)

	@Query("SELECT login FROM login")
	fun getLogins(): List<String>

	@Query("SELECT password FROM login WHERE login = :login")
	fun getPassword(login: String): String

	@Query("DELETE FROM login WHERE login = :login")
	fun removeLogin(login: String)
}