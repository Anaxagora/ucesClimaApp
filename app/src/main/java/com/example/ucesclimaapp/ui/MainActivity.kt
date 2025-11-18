package com.example.ucesclimaapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ucesclimaapp.WeatherActivity
import com.example.ucesclimaapp.databinding.ActivityMainBinding
import com.example.ucesclimaapp.model.Location
import com.example.ucesclimaapp.viewmodel.LocationViewModel
import com.example.ucesclimaapp.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var adapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --------------------------
        // 🟦 Inicializo ambos ViewModels
        // --------------------------
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        setupRecycler()

        // --------------------------
        // 🟦 Observa cambios en la base local
        // --------------------------
        locationViewModel.locationsLiveData.observe(this) { list ->
            adapter.updateList(list)
        }

        // --------------------------
        // 🟦 Cargar ciudades guardadas
        // --------------------------
        locationViewModel.loadLocations()

        // --------------------------
        // 🟦 Botón para guardar ciudad
        // --------------------------
        binding.btnSave.setOnClickListener {
            saveLocation()
        }
    }

    private fun setupRecycler() {
        adapter = LocationAdapter(
            emptyList(),
            onItemClick = { location ->

                // --------------------------
                // 🟦 Probar primero si la API responde
                // --------------------------
                weatherViewModel.testWeather(
                    apiKey = "d3bfcf12-bf51-11f0-a0d3-0242ac130003-d3bfcfda-bf51-11f0-a0d3-0242ac130003",
                    lat = location.lat,
                    lon = location.lon
                ) { success: Boolean ->

                    if (success) {
                        // ✔ Si la API funciona → pasar a WeatherActivity
                        val intent = Intent(this, WeatherActivity::class.java)
                        intent.putExtra("city", location.city)
                        intent.putExtra("lat", location.lat)
                        intent.putExtra("lon", location.lon)
                        startActivity(intent)

                    } else {
                        // ❌ Error en la API → no avanzar
                        Toast.makeText(
                            this,
                            "No se pudo obtener información del clima. Intente más tarde.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },

            // --------------------------
            // 🟦 Eliminar ciudad
            // --------------------------
            onDeleteClick = { location ->
                locationViewModel.deleteLocation(location)
            }
        )

        binding.rvLocations.layoutManager = LinearLayoutManager(this)
        binding.rvLocations.adapter = adapter
    }

    private fun saveLocation() {
        val city = binding.etCityName.text.toString()
        val lat = binding.etLatitude.text.toString().toDoubleOrNull()
        val lon = binding.etLongitude.text.toString().toDoubleOrNull()

        if (city.isBlank() || lat == null || lon == null) return

        val location = Location(city = city, lat = lat, lon = lon)
        locationViewModel.addLocation(location)

        // Limpiar inputs
        binding.etCityName.text.clear()
        binding.etLatitude.text.clear()
        binding.etLongitude.text.clear()
    }
}
