package com.yans.navobserver.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yans.navobserver.room.entities.LatLonAlt
import com.yans.navobserver.room.entities.LocationInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationViewModel(
    private val repository: ServiceRepository
) : ViewModel() {

    val locations: StateFlow<PagingData<LocationInfo>> = Pager(
        PagingConfig(pageSize = 16)
    ) {
        repository.getPagedAllInfo()
    }.flow
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())


    private val _distance: MutableLiveData<Double> = MutableLiveData()
    val distance: LiveData<Double>
        get() = _distance

    private val _speed: MutableLiveData<Double> = MutableLiveData()
    val speed: LiveData<Double>
        get() = _speed

    fun clearInfo() = viewModelScope.launch {
        repository.clearAllData()
    }

    fun calculateDistance() = viewModelScope.launch {
        val latLon = repository.getLatLon()
        val dist = calculateDistance(latLon)
        _distance.value = dist
        val times = repository.getTimes()
        if(times.size > 1){
            _speed.value = calculateSpeed(dist, times.last() - times.first())
        }
    }

    private fun calculateSpeed(distance: Double, timeDiff: Long) : Double {
        return distance / (timeDiff / 1000 / 60 / 60)
    }

    private suspend fun calculateDistance(latLon: List<LatLonAlt>): Double =
        withContext(Dispatchers.Default) {
            var result = 0.0
            for (i in 1 until latLon.size) {
                val dist = getDistanceFromLatLonInKm(latLon[i - 1], latLon[i])
                result += dist
            }
            result
        }

    private fun getDistanceFromLatLonInKm(
        latLon1: LatLonAlt,
        latLon2: LatLonAlt
    ): Double {
        val radius = 6371
        val dLat = deg2rad(latLon2.latitude - latLon1.latitude)
        val dLon = deg2rad(latLon2.longitude - latLon1.longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(deg2rad(latLon1.latitude)) * cos(deg2rad(latLon2.latitude)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }

    private fun deg2rad(deg: Double): Double {
        return deg * (Math.PI / 180)
    }

}