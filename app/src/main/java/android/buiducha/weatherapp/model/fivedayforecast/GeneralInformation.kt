package android.buiducha.weatherapp.model.fivedayforecast

import android.buiducha.weatherapp.model.currentweather.KeyInformation
import android.buiducha.weatherapp.model.currentweather.Weather
import android.buiducha.weatherapp.model.currentweather.Wind
import com.google.gson.annotations.SerializedName

data class GeneralInformation(
    @SerializedName("dt")
    val weatherDate: Long,
    @SerializedName("dt_txt")
    val weatherDateTxt: String,
    val main: KeyInformation,
    val pop: Double,
    @SerializedName("sys")
    val partOfDay: PartOfDay,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)