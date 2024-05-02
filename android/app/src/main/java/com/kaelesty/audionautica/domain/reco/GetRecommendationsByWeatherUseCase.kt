package com.kaelesty.audionautica.domain.reco

import javax.inject.Inject

class GetRecommendationsByWeatherUseCase @Inject constructor(
	private val repo: IRecoRepository
) {

	suspend operator fun invoke(weather: Weather, num: Int = 10) =
		repo.getRecommendationsByWeather(weather, num)
}