package com.example.ucesclimaapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ucesclimaapp.model.Location
import com.example.ucesclimaapp.repository.LocationRepository

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LocationRepository(application)

    val locationsLiveData = MutableLiveData<List<Location>>()

    fun loadLocations() {
        locationsLiveData.value = repository.getAll()
    }

    fun addLocation(location: Location) {
        repository.insert(location)
        loadLocations()
    }
    fun deleteLocation(location: Location) {
        repository.delete(location)
        loadLocations()
    }

}
