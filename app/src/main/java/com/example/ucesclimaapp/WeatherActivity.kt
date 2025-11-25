package com.example.ucesclimaapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ucesclimaapp.databinding.ActivityWeatherBinding
import com.example.ucesclimaapp.api.HourData
import android.content.Intent
import com.example.ucesclimaapp.api.WeatherResponse
import com.example.ucesclimaapp.WeatherViewModel
import java.time.OffsetDateTime

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val city = intent.getStringExtra("city")
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lon = intent.getDoubleExtra("lon", 0.0)

        binding.tvCityTitle.text = city ?: "Ciudad"

        // ✔ Observers del ViewModel
        viewModel.weatherLiveData.observe(this) { response ->
            val hour = findClosestHour(response.hours)

            if (hour != null) {
                binding.tvTemp.text = "Temp: ${hour.airTemperature?.noaa ?: "---"}°C"
                binding.tvHumidity.text = "Humedad: ${hour.humidity?.noaa ?: "---"}%"
                binding.tvWind.text = "Viento: ${hour.windSpeed?.noaa ?: "---"} m/s"
                binding.tvCloud.text = "Nubosidad: ${hour.cloudCover?.noaa ?: "---"}%"
                binding.tvVisibility.text = "Visibilidad: ${hour.visibility?.noaa ?: "---"} km"
                binding.tvRain.text = "Lluvia: ${hour.precipitation?.noaa ?: "---"} mm"
                binding.tvPressure.text = "Presión: ${hour.pressure?.noaa ?: "---"} hPa"
                binding.tvHourInfo.text = "Horario: ${hour.time}"
            }
        }
        viewModel.errorLiveData.observe(this) { error ->
            binding.tvTemp.text = "Error"
            binding.tvHumidity.text = error
        }

        // ---- LLAMADA REAL A LA API ----
        viewModel.loadWeather(
            apiKey = Constants.API_KEY,
            lat = lat,
            lon = lon
        )
        // Boton para volver atras
        binding.btnBack.setOnClickListener { finish() }
        // ⭐ NUEVO: ir a ForecastActivity (pantalla 3)
        binding.btnForecast.setOnClickListener {
            val intent = Intent(this, ForecastActivity::class.java)
            intent.putExtra("lat", lat)
            intent.putExtra("lon", lon)
            intent.putExtra("city", city)
            intent.putExtra("apiKey", Constants.API_KEY)
            startActivity(intent)
        }
    }

    // ✅ NUEVA FUNCIÓN: selecciona la hora más cercana a la actual
    private fun findClosestHour(hours: List<HourData>): HourData? {
        val format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        format.timeZone = java.util.TimeZone.getTimeZone("UTC")

        val now = System.currentTimeMillis()

        return hours.minByOrNull { hour ->
            val millis = try {
                format.parse(hour.time)?.time ?: Long.MAX_VALUE
            } catch (e: Exception) {
                Long.MAX_VALUE
            }
            kotlin.math.abs(millis - now)
        }
    }

}
