package com.example.ucesclimaapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucesclimaapp.api.RetrofitClient
import com.example.ucesclimaapp.api.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    val weatherLiveData = MutableLiveData<WeatherResponse>()
    val errorLiveData = MutableLiveData<String>()

    fun loadWeather(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getWeather(apiKey, lat, lon)
                weatherLiveData.postValue(response)
            } catch (e: Exception) {
                errorLiveData.postValue(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun testWeather(apiKey: String, lat: Double, lon: Double, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getWeather(apiKey, lat, lon)
                onResult(true)   // éxito
            } catch (e: Exception) {
                onResult(false)  // error
            }
        }
    }

}

