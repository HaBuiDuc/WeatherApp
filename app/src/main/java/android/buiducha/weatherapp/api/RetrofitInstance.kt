package android.buiducha.weatherapp.api

import android.buiducha.weatherapp.utils.Constants.openWeatherUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(openWeatherUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api: OpenWeatherApi by lazy {
        retrofit.create(OpenWeatherApi::class.java)
    }
}