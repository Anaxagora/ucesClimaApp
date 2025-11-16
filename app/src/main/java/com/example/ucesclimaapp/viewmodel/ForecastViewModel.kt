package com.example.ucesclimaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucesclimaapp.api.RetrofitClient
import com.example.ucesclimaapp.api.HourData
import com.example.ucesclimaapp.model.DailyForecast
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ForecastViewModel : ViewModel() {

    val forecastLiveData = MutableLiveData<List<DailyForecast>>()
    val errorLiveData = MutableLiveData<String>()

    fun loadForecast(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
                format.timeZone = TimeZone.getTimeZone("UTC")

                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

                // START (hoy a las 00hs UTC)
                val startIso = format.format(calendar.time)

                // END (dentro de 10 días)
                calendar.add(Calendar.DAY_OF_YEAR, 10)
                val endIso = format.format(calendar.time)

                val response = RetrofitClient.api.getForecast10Days(
                    apiKey,
                    lat,
                    lon,
                    start = startIso,
                    end = endIso
                )

                forecastLiveData.postValue(
                    convertHoursToDailyForecast(response.hours)
                )

            } catch (e: Exception) {
                errorLiveData.postValue(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    private fun convertHoursToDailyForecast(hours: List<HourData>): List<DailyForecast> {
        val dailyMap = mutableMapOf<String, MutableList<HourData>>()

        // 1. Agrupar por día
        for (hour in hours) {
            val day = hour.time.substring(0, 10) // "2025-02-01"
            if (!dailyMap.containsKey(day)) dailyMap[day] = mutableListOf()
            dailyMap[day]!!.add(hour)
        }

        // 2. Calcular promedios
        val result = mutableListOf<DailyForecast>()

        for ((date, hourList) in dailyMap) {
            val tempAvg = hourList.mapNotNull { it.airTemperature?.noaa }.averageOrNull()
            val rainAvg = hourList.mapNotNull { it.precipitation?.noaa }.averageOrNull()

            result.add(
                DailyForecast(
                    date = date,
                    tempAvg = tempAvg,
                    rainAvg = rainAvg
                )
            )
        }

        return result.sortedBy { it.date }
    }

    private fun List<Double>.averageOrNull(): Double? =
        if (isNotEmpty()) this.average() else null
}
