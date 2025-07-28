package com.parkhang.module.framework.geolocation

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GeolocationModule {
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context,
    ): FusedLocationProviderClient {
        // Assuming you have a way to provide the FusedLocationProviderClient instance
        // This could be through a context or application instance
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideGeolocationTracker(fusedLocationProviderClient: FusedLocationProviderClient): GeolocationTracker =
        GeolocationTracker(
            fusedLocationProviderClient = fusedLocationProviderClient,
        )
}
