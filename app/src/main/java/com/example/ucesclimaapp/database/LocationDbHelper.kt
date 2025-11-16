package com.example.ucesclimaapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ucesclimaapp.model.Location

class LocationDbHelper(context: Context)
    : SQLiteOpenHelper(context, "locations.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE locations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "city TEXT," +
                    "lat REAL," +
                    "lon REAL)"
        )
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun insertLocation(location: Location) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("city", location.city)
            put("lat", location.lat)
            put("lon", location.lon)
        }
        db.insert("locations", null, values)
    }
    fun getAll(): List<Location> {
        val list = mutableListOf<Location>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM locations", null)

        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Location(
                        id = cursor.getInt(0),
                        city = cursor.getString(1),
                        lat = cursor.getDouble(2),
                        lon = cursor.getDouble(3)
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return list
    }
    fun delete(location: Location) {
        val db = writableDatabase
        db.delete(
            "locations",
            "id = ?",
            arrayOf(location.id.toString())
        )
    }


}