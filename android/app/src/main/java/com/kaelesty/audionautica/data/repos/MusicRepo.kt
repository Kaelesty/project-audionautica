package com.kaelesty.audionautica.data.repos

import com.kaelesty.audionautica.di.ApplicationScope
import com.kaelesty.audionautica.domain.repos.IMusicRepo
import javax.inject.Inject

@ApplicationScope
class MusicRepo @Inject constructor(): IMusicRepo {

	override suspend fun logout() {
		// TODO delete JWT
	}
}