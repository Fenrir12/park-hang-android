package com.parkhang.mobile.feature.parks.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parkhang.mobile.feature.parks.datasource.ParksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParksViewModel
    @Inject
    constructor(
        private val parksRepository: ParksRepository,
    ) : ViewModel() {
        var parksQuantity: String? = null

        init {
            viewModelScope.launch {
                try {
                    parksQuantity =
                        parksRepository
                            .fetchNearbyParks(
                                "45.54",
                                "-73.55",
                                "500",
                            )?.parkList
                            ?.size
                            ?.toString()
                            .orEmpty()
                } catch (exception: Exception) {
                    Log.e("TEST", "Error fetching parks: ${exception.message}", exception)
                }
            }
        }
    }
