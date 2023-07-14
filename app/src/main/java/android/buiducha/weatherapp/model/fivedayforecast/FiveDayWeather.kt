package android.buiducha.weatherapp.model.fivedayforecast

import com.google.gson.annotations.SerializedName

data class FiveDayWeather(
    val city: City,
    @SerializedName("list")
    val weatherByHour: List<GeneralInformation>
)