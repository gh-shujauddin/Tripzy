package com.qadri.tripzy.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DatabaseRepository {
    fun getTrips(day: String, destination: String): Flow<List<TripsEntity?>>

    fun getMoreInfo(destination: String): Flow<List<TripsEntity?>>

    val allTrips: Flow<List<TripsEntity?>>
    fun getCurrentTrip(destination: String): Flow<List<TripsEntity?>>
    fun distinctDays(destination: String): Flow<List<String?>>

    fun insertTrip(tourDetails: TripsEntity)

    fun swapTripPositions(day: String, fromIndex: Int, toIndex: Int, destination: String)

    fun updateTrips(
        name: String, budget: String?, latitude: Double?, longitude: Double?,
        photoBase64: String?, distance: String, duration: String, timeOfDay: String,
        fromId: Long, fromDay: String, fromDestination: String
    )
    

    fun getBudget(destination: String): Flow<List<String?>>

    fun getTotalBudget(destination: String): Flow<List<Double?>>
    fun insertAllTrips(trips: List<TripsEntity>)

    fun getDepartureDate(day: String, destination: String): Flow<List<String?>>

    fun getArrivalDate(day: String, destination: String): Flow<List<String?>>

}

class DatabaseRepositoryImpl @Inject constructor(
    private val tripsDao: TripzyDao
) : DatabaseRepository {
    override fun getTrips(day: String, destination: String): Flow<List<TripsEntity?>> =
        tripsDao.getTrips(day, destination)

    override fun getMoreInfo(destination: String): Flow<List<TripsEntity?>> =
        tripsDao.getMoreInfo(destination)

    override val allTrips: Flow<List<TripsEntity?>> = tripsDao.getAllTrips()
    override fun getCurrentTrip(destination: String): Flow<List<TripsEntity?>> =
        tripsDao.getCurrentTrip(destination)

    override fun distinctDays(destination: String): Flow<List<String?>> = tripsDao.getUniqueDays(destination)

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun insertTrip(tourDetails: TripsEntity) {
        coroutineScope.launch {
            tripsDao.insertTrip(tourDetails)
        }
    }

    override fun swapTripPositions(day: String, fromIndex: Int, toIndex: Int, destination: String) {
        coroutineScope.launch {
            tripsDao.swapTripPositions(day, fromIndex, toIndex, destination)
        }
    }

    override fun updateTrips(
        name: String, budget: String?, latitude: Double?, longitude: Double?,
        photoBase64: String?, distance: String, duration: String, timeOfDay: String,
        fromId: Long, fromDay: String, fromDestination: String
    ) {
        coroutineScope.launch {
            tripsDao.updateTrips(
                name,
                budget,
                latitude,
                longitude,
                photoBase64,
                distance,
                duration,
                timeOfDay,
                fromId,
                fromDay,
                fromDestination
            )
        }
    }

    override fun getBudget(destination: String): Flow<List<String?>> =
        tripsDao.getBudget(destination)

    override fun getTotalBudget(destination: String): Flow<List<Double?>> =
        tripsDao.getTotalBudget(destination)

    override fun insertAllTrips(trips: List<TripsEntity>) {
        coroutineScope.launch {
            tripsDao.insertAllTrips(trips)
        }
    }

    override fun getDepartureDate(day: String, destination: String): Flow<List<String?>> =
        tripsDao.getDepartureDate(day, destination)

    override fun getArrivalDate(day: String, destination: String): Flow<List<String?>> =
        tripsDao.getArrivalDate(day, destination)

}