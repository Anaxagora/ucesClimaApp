package com.example.ucesclimaapp.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("weather/point")
    suspend fun getWeather(
        @Header("Authorization") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lng") lon: Double,
        @Query("params") params: String = "airTemperature,cloudCover,humidity,precipitation,visibility,windDirection,windSpeed"
    ): WeatherResponse

    @GET("weather/point")
    suspend fun getForecast10Days(
        @Header("Authorization") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lng") lon: Double,
        @Query("params") params: String =
            "airTemperature,precipitation",
        @Query("start") start: String,
        @Query("end") end: String
    ): WeatherResponse

}
