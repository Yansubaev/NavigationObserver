package com.yans.navobserver.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yans.navobserver.room.entities.LatLonAlt
import com.yans.navobserver.room.entities.LocationInfo

@Dao
interface LocationInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationInfo: LocationInfo)

    @Query("SELECT * FROM LocationInfo")
    suspend fun getAll() : List<LocationInfo>

    @Query("DELETE FROM LocationInfo")
    suspend fun deleteAll()

    @Query("SELECT * FROM LocationInfo")
    fun getPaged() : PagingSource<Int, LocationInfo>

    @Query("SELECT latitude, longitude, altitude FROM LocationInfo")
    suspend fun getLatLon() : List<LatLonAlt>

    @Query("SELECT time FROM LocationInfo")
    suspend fun getTimes() : List<Long>

}