package com.yans.navobserver.service

import android.location.Location
import com.yans.navobserver.room.dao.LocationInfoDao
import com.yans.navobserver.room.entities.LocationInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServiceRepository(private val dao: LocationInfoDao) {

    suspend fun insertInfo(location: Location, time: Long) = withContext(Dispatchers.IO) {
        dao.insert(LocationInfo(location.latitude, location.longitude, location.altitude, time))
    }

    suspend fun getAllInfo() : List<LocationInfo> = withContext(Dispatchers.IO) {
        dao.getAll()
    }

    suspend fun clearAllData() = withContext(Dispatchers.IO){
        dao.deleteAll()
    }

    fun getPagedAllInfo() = dao.getPaged()

    suspend fun getLatLon() = dao.getLatLon()

    suspend fun getTimes() = dao.getTimes()
}