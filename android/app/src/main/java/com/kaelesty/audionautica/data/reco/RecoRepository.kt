package com.kaelesty.audionautica.data.reco

import com.kaelesty.audionautica.data.mappers.TrackMapper
import com.kaelesty.audionautica.data.repos.tools.JwtTool
import com.kaelesty.audionautica.di.ApplicationScope
import com.kaelesty.audionautica.domain.entities.Track
import com.kaelesty.audionautica.domain.reco.IRecoRepository
import com.kaelesty.audionautica.domain.reco.Weather
import java.io.IOException
import javax.inject.Inject

@ApplicationScope
class RecoRepository @Inject constructor(
	private val apiService: RecoApiService,
	private val jwtTool: JwtTool,
	private val trackMapper: TrackMapper
): IRecoRepository {

	override suspend fun getRecommendationsByWeather(weather: Weather, num: Int): List<Track> {
		return try {
			val res = apiService.getWeatherReco(
				token = jwtTool.getToken(),
				body = WeatherRecoRequest(
					windDirection = weather.windDirection,
					windForce = weather.windForce,
					pressure = weather.pressure,
					waves = weather.waves,
					temp = weather.temp,
					num = num
				)
			)
			when (res.code()) {
				200 -> {
					return res.body()?.let {
						it.tracks.map { trackMapper.dtoToDomain(it) }
					} ?: throw IOException()
				}
				else -> {
					throw IOException()
				}
			}
		}
		catch (e: Exception) {
			listOf()
		}
	}
}