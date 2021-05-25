package com.yans.navobserver.gps

import android.location.Location
import androidx.lifecycle.MutableLiveData

object LocationData {
    val locationData: MutableLiveData<Location> = MutableLiveData()
}