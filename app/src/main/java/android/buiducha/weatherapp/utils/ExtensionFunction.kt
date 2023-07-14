package android.buiducha.weatherapp.utils

import android.annotation.SuppressLint
import android.buiducha.weatherapp.R
import android.buiducha.weatherapp.model.currentweather.CurrentWeather
import android.buiducha.weatherapp.model.fivedayforecast.GeneralInformation
import java.text.SimpleDateFormat
import java.util.Date

fun GeneralInformation.getImage(): Int {
    val weatherId = weather[0].id
    val time = weatherDateTxt.toString().substringAfterLast(" ").substring(0, 2).toInt()
    val isDay = time in 6..18

    return getWeather(weatherId, isDay)
}

fun GeneralInformation.temperatureFormat() = main.temp.toString().substringBeforeLast('.')

@SuppressLint("SimpleDateFormat")
fun GeneralInformation.getDayOfWeek(): String {
    val sdf = SimpleDateFormat("EEEE")
    val date = Date(weatherDate * 1000)
    return sdf.format(date)
}

fun CurrentWeather.getImage(time: Int): Int {
    val weatherId = weather[0].id
    val isDay = time in 6..18

    return getWeather(weatherId, isDay)
}

fun CurrentWeather.temperatureFormat() = keyInformation.temp.toString().substringBeforeLast('.')

private fun getWeather(weatherId: Int, isDay: Boolean) = when (weatherId.toString()[0]) {
    '2' -> R.drawable.thunderstorm_img
    '3' -> if (isDay) R.drawable.drizzle_day_img else R.drawable.drizzle_night_img
    '5' -> if (isDay) R.drawable.rainy_day_img else R.drawable.rainy_night_img
    '6' -> if (isDay) R.drawable.snow_day_img else R.drawable.snow_night_img
    '7' -> if (weatherId == 781) R.drawable.tornado_img else R.drawable.fog_img
    '8' -> if (weatherId == 800) {
        if (isDay) R.drawable.clear_day_sky_img else R.drawable.clear_night_sky_img
    } else {
        if (isDay) R.drawable.cloud_day_img else R.drawable.cloud_night_img
    }
    else -> R.drawable.cloud_day_img
}


