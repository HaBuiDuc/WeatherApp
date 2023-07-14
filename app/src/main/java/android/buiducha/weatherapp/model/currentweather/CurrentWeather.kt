package android.buiducha.weatherapp.model.currentweather

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    val dt: Int,
    @SerializedName("main")
    val keyInformation: KeyInformation,
    val name: String,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)