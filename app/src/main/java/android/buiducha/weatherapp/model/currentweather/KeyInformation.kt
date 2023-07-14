package android.buiducha.weatherapp.model.currentweather

import com.google.gson.annotations.SerializedName

data class KeyInformation(
    @SerializedName("feels_like")
    val feelLike: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin: Double
)