package com.yans.navobserver.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yans.navobserver.room.entities.LocationInfo
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repository: ServiceRepository
) : ViewModel() {

    private val _locationData: MutableLiveData<List<LocationInfo>> = MutableLiveData()
    val locationData: LiveData<List<LocationInfo>>
        get() = _locationData

    init {
        _locationData.value = null
        viewModelScope.launch {
            _locationData.value = repository.getAllInfo()
        }
    }

    fun fetchInfos() = viewModelScope.launch {
        _locationData.value = repository.getAllInfo()
    }

    fun clearInfos() = viewModelScope.launch {
        _locationData.value = null
        repository.clearAllData()
        fetchInfos()
    }
}