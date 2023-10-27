package com.kaelesty.audionautica.data.repos

import com.kaelesty.audionautica.domain.repos.IMusicRepo

class MusicRepo: IMusicRepo {

	override suspend fun logout() {
		// TODO delete JWT
	}
}