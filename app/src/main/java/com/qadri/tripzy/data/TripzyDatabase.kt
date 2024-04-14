package com.qadri.tripzy.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.qadri.tripzy.domain.Recents

@Database(entities = [Recents::class, TripsEntity::class], version = 2)
abstract class TripzyDatabase: RoomDatabase() {
    abstract fun getTripzyDao(): TripzyDao
}