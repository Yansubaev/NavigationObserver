package com.yans.navobserver.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yans.navobserver.room.entities.LocationInfo

@Dao
interface LocationInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationInfo: LocationInfo)

    @Query("SELECT * FROM LocationInfo")
    suspend fun getAll() : List<LocationInfo>

    @Query("DELETE FROM LocationInfo")
    suspend fun deleteAll()

}