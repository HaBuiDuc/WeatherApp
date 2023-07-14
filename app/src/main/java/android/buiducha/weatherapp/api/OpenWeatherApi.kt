package android.buiducha.weatherapp.api

import android.buiducha.weatherapp.model.CityLocation
import android.buiducha.weatherapp.model.currentweather.CurrentWeather
import android.buiducha.weatherapp.model.fivedayforecast.FiveDayWeather
import android.buiducha.weatherapp.utils.Constants.apiKey
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = apiKey,
        @Query("units") units: String = "metric"
    ): CurrentWeather

    @GET("data/2.5/forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = apiKey,
        @Query("units") units: String = "metric",
    ): FiveDayWeather

    @GET("geo/1.0/direct")
    suspend fun getGeoCoding(
        @Query("q") cityName: String,
        @Query("appid") appId: String = apiKey,
        @Query("limit") limit: Int = 1,
    ): List<CityLocation>
}