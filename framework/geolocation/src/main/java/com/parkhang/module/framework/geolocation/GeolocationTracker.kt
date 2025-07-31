package com.parkhang.module.framework.geolocation

import android.Manifest
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GeolocationTracker(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) {
    @RequiresPermission(
        allOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ],
    )
    val geolocation: Flow<Location> = callbackFlow {
        var attempts = 0
        val maxAttempts = 3

        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
        fun fetchCurrentLocation() {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 0).build()
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val currentLocation = locationResult.lastLocation
                    if (currentLocation != null) {
                        trySend(currentLocation).isSuccess
                        close()
                        fusedLocationProviderClient.removeLocationUpdates(this)
                    }
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    if (!locationAvailability.isLocationAvailable) {
                        close(Exception("Current location not available"))
                        fusedLocationProviderClient.removeLocationUpdates(this)
                    }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }

        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
        fun fetchLastLocation() {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        trySend(location).isSuccess
                        close()
                    } else {
                        attempts++
                        if (attempts < maxAttempts) {
                            fetchLastLocation()
                        } else {
                            fetchCurrentLocation()
                        }
                    }
                }.addOnFailureListener {
                    attempts++
                    if (attempts < maxAttempts) {
                        fetchLastLocation()
                    } else {
                        fetchCurrentLocation()
                    }
                }
        }

        fetchLastLocation()

        awaitClose {
            // Clean-up: remove location updates if any
            fusedLocationProviderClient.removeLocationUpdates { }
        }
    }
}
