package com.parkhang.module.framework.geolocation

import android.Manifest
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GeolocationTracker(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) {
    val geolocation: Flow<Location>
        @RequiresPermission(
            allOf = [
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ],
        )
        get() =
            callbackFlow {
                var attempts = 0
                val maxAttempts = 3

                fun fetchLocation() {
                    fusedLocationProviderClient.lastLocation
                        .addOnSuccessListener { location ->
                            if (location != null) {
                                trySend(location).isSuccess
                                close()
                            } else {
                                attempts++
                                if (attempts < maxAttempts) {
                                    fetchLocation()
                                } else {
                                    close(Exception("Location not available after $maxAttempts attempts"))
                                }
                            }
                        }.addOnFailureListener { exception ->
                            attempts++
                            if (attempts < maxAttempts) {
                                fetchLocation()
                            } else {
                                close(exception)
                            }
                        }
                }

                fetchLocation()

                awaitClose { /* Clean-up if needed */ }
            }
}
