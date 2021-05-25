package com.yans.navobserver.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class LocationInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "altitude")
    val altitude: Double,
    @ColumnInfo(name = "time")
    val time: Long
) {
    constructor(latitude: Double, longitude: Double, altitude: Double, time: Long) :
            this(0, latitude, longitude, altitude, time)

    fun contentEquals(other: LocationInfo) : Boolean {
        if(id != other.id) return false
        if(latitude != other.latitude) return false
        if(longitude != other.longitude) return false
        if(altitude != other.altitude) return false
        if(time != other.time) return false

        return true
    }
}