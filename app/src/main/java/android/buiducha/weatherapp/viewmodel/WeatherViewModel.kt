package android.buiducha.weatherapp.viewmodel

import android.annotation.SuppressLint
import android.buiducha.weatherapp.model.CityLocation
import android.buiducha.weatherapp.model.currentweather.CurrentWeather
import android.buiducha.weatherapp.model.fivedayforecast.FiveDayWeather
import android.buiducha.weatherapp.repository.WeatherFetchRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel : ViewModel() {
    private val weatherRepository = WeatherFetchRepository()

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("HH")
    var currentDateTime: String = sdf.format(Date())

    private val _currentWeather = MutableStateFlow<CurrentWeather?>(null)
    val currentWeather: StateFlow<CurrentWeather?> get() =  _currentWeather

    private val _fiveDayForecast = MutableStateFlow<FiveDayWeather?>(null)
    val fiveDayForecast: StateFlow<FiveDayWeather?> get() = _fiveDayForecast

    fun getCurrentWeather(lat: Double, lon: Double) {
        currentDateTime = sdf.format(Date())
        Log.d(TAG, currentDateTime)
        viewModelScope.launch {
            _currentWeather.value = weatherRepository.getCurrentWeather(lat, lon)
        }
    }

    fun getFiveDayForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            _fiveDayForecast.value = weatherRepository.getFiveDayForecast(lat, lon)
        }
    }

    suspend fun getGeoCoding(cityName: String): CityLocation {
        return weatherRepository.getGeoCoding(cityName)[0]
    }

    companion object {
        const val TAG = "WeatherViewModel"
    }
}