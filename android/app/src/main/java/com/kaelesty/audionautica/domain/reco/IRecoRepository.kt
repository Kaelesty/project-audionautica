package com.kaelesty.audionautica.domain.reco

import com.kaelesty.audionautica.domain.entities.Track

interface IRecoRepository {

	suspend fun getRecommendationsByWeather(weather: Weather, num: Int): List<Track>
}