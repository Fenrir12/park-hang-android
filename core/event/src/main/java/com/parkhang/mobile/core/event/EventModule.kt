package com.parkhang.mobile.core.event

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EventModule {
    @Provides
    @Singleton
    fun providesAppEventBus(): AppEventBus = AppEventBus()

    sealed interface AppEvent {
        data class CheckInCompleted(
            val parkId: String,
            val parkName: String,
        ) : AppEvent

        object UserLoggedOut : AppEvent
    }
}
