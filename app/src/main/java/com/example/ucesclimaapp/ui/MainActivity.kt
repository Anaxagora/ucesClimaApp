package com.example.ucesclimaapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ucesclimaapp.WeatherActivity
import com.example.ucesclimaapp.database.LocationDbHelper
import com.example.ucesclimaapp.databinding.ActivityMainBinding
import com.example.ucesclimaapp.model.Location
import com.example.ucesclimaapp.viewmodel.LocationViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LocationViewModel
    private lateinit var adapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LocationViewModel::class.java]

        setupRecycler()

        viewModel.locationsLiveData.observe(this) { list ->
            adapter.updateList(list)
        }

        viewModel.loadLocations()

        binding.btnSave.setOnClickListener {
            saveLocation()
        }
    }

    private fun setupRecycler() {
        adapter = LocationAdapter(
            emptyList(),
            onItemClick = { location ->
                val intent = Intent(this, WeatherActivity::class.java)
                intent.putExtra("city", location.city)
                intent.putExtra("lat", location.lat)
                intent.putExtra("lon", location.lon)
                startActivity(intent)
            },
            onDeleteClick = { location ->
                viewModel.deleteLocation(location)
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
        viewModel.addLocation(location)

        binding.etCityName.text.clear()
        binding.etLatitude.text.clear()
        binding.etLongitude.text.clear()
    }
}
