package com.example.ucesclimaapp.api

import android.health.connect.datatypes.units.Pressure

data class WeatherResponse(
    val hours: List<HourData>
)

data class HourData(
    val time: String,
    val airTemperature: DataPoint?,
    val cloudCover: DataPoint?,
    val humidity: DataPoint?,
    val precipitation: DataPoint?,
    val visibility: DataPoint?,
    val windDirection: DataPoint?,
    val windSpeed: DataPoint?,
    val pressure: DataPoint?
)

data class DataPoint(
    val noaa: Double?
)
