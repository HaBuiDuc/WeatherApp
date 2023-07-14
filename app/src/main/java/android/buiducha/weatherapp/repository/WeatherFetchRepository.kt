package android.buiducha.weatherapp.repository

import android.buiducha.weatherapp.api.RetrofitInstance

class WeatherFetchRepository {
    suspend fun getGeoCoding(cityName: String) = RetrofitInstance.api.getGeoCoding(cityName)

    suspend fun getCurrentWeather(lat: Double, lon: Double) = RetrofitInstance.api.getCurrentWeather(lat, lon)

    suspend fun getFiveDayForecast(lat: Double, lon: Double) = RetrofitInstance.api.getFiveDayForecast(lat, lon)


}