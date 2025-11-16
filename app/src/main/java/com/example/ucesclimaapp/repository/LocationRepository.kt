package com.example.ucesclimaapp.repository

import android.content.Context
import com.example.ucesclimaapp.database.LocationDbHelper
import com.example.ucesclimaapp.model.Location

class LocationRepository(context: Context) {

    private val db = LocationDbHelper(context)

    fun insert(location: Location) {
        db.insertLocation(location)
    }

    fun getAll(): List<Location> {
        return db.getAll()
    }
    fun delete(location: Location) {
        db.delete(location)
    }


}
