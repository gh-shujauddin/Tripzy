package com.qadri.tripzy.ui

import android.Manifest
import android.app.Application
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private var locationState = mutableStateOf(CurrentLocation())

    private var locationCallback: LocationCallback
    private var counter = 0

    val locationFlow = callbackFlow {
        while (true) {
            ++counter

        val location = Location("LocationProvider")
        location.apply {
            latitude = locationState.value.latitude ?: 0.0
            longitude = locationState.value.longitude ?: 0.0
        }

        Log.d(ContentValues.TAG, "Location $counter: ${location.longitude}")
        trySend(location)

//        awaitClose { close() }
            delay(5000)
        }
    }.shareIn(
        viewModelScope,
        replay = 0,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    setLocationData(location)
                }
            }
        }
        startLocationUpdate()
    }


    fun startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun setLocationData(location: Location?) {
        location?.let { newLocation ->
            locationState.value = CurrentLocation(
                latitude = newLocation.latitude,
                longitude = newLocation.longitude
            )
        }
    }

    companion object {
        private const val ONE_MINUTE: Long = 60_000L
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, ONE_MINUTE)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(ONE_MINUTE / 4)
            .setMaxUpdateDelayMillis(ONE_MINUTE / 2)
            .build()

    }
}
data class CurrentLocation(
    val latitude: Double? = null,
    val longitude: Double? = null
)