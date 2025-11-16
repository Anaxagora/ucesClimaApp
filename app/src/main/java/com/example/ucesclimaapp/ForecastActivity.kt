package com.example.ucesclimaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ucesclimaapp.databinding.ActivityForecastBinding
import com.example.ucesclimaapp.viewmodel.ForecastViewModel

class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastBinding
    private val viewModel: ForecastViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lat = intent.getDoubleExtra("lat", 0.0)
        val lon = intent.getDoubleExtra("lon", 0.0)
        val apiKey = intent.getStringExtra("apiKey") ?: ""

        val adapter = ForecastAdapter(emptyList())
        binding.recyclerForecast.layoutManager = LinearLayoutManager(this)
        binding.recyclerForecast.adapter = adapter

        binding.btnBack.setOnClickListener { finish() }

        viewModel.forecastLiveData.observe(this) {
            adapter.updateData(it)
        }

        viewModel.errorLiveData.observe(this) {
            binding.tvError.text = it
        }

        viewModel.loadForecast(apiKey, lat, lon)
    }
}

