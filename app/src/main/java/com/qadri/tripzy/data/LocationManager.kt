package com.qadri.tripzy.data

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import java.util.Locale

class LocationManager(private val context: Context, private val activity: Activity) {

    private val settingsClient = LocationServices.getSettingsClient(context)
    private val locationRequest = LocationRequest()

    fun checkGpsSettings() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            .setAlwaysShow(false)
            .setNeedBle(false)
        settingsClient.checkLocationSettings(builder.build())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result ?: return@addOnCompleteListener
                    val locationSettingsStates = response.locationSettingsStates
                    Log.e("yyy", locationSettingsStates.toString())
                    // TODO
                }
            }
            .addOnFailureListener { e ->
                Log.e("Location status", "checkLocationSetting onFailure:" + e.message.toString())
                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.d(
                            "Location status",
                            "Location settings are not satisfied. Attempting to upgrade " + "location settings "
                        )
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(activity, 0)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.d("Location status", "PendingIntent unable to execute request.")
                        }
                    }

                    else -> {
                    }
                }
            }
    }

    val gpsStatus = flow {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        while (currentCoroutineContext().isActive) {
            emit(
                manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            )
            delay(3000)
        }
    }

    fun getAddressFromCoordinate(
        latitude: Double,
        longitude: Double
    ): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var address: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude, longitude, 1) {
                address = it.firstOrNull()?.getAddressLine(0)
            }

            return address ?: "No address found"
        }

        return try {
            @Suppress("DEPRECATION")
            address = geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()?.getAddressLine(0)
            address ?: "No address found"
        } catch (e: Exception) {
            //will catch if there is an internet problem
            "No address found"
        }
    }
}