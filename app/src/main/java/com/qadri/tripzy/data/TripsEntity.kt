package com.qadri.tripzy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val day: String,
    var timeOfDay: String,
    var name: String,
    val source: String,
    val destination: String,
    var travelActivity: String,
    var budget: String? = null,
    var latitude: Double?,
    var longitude: Double?,
    var photoBase64: String? = null, // Store photo as Base64 String,
    var distance: String = "0",
    var duration: String = "0",
    var totalBudget: Double? = 0.0,
    var departureDate: String? = null,
    var arrivalDate: String? = null,
    var departureTime: String? = null,
    var arrivalTime: String? = null,
)