package com.kaelesty.audionautica.data.repos.tools

import android.util.Log
import com.kaelesty.audionautica.data.local.daos.AccessDao
import com.kaelesty.audionautica.data.local.daos.PlaylistDao
import com.kaelesty.audionautica.data.local.dbmodels.TokenDbModel
import javax.inject.Inject

class CacheTool @Inject constructor(
	private val playlistsDao: PlaylistDao,
) {

	fun clearPlaylistsCache() {
		playlistsDao.nukeTable()
	}
}