package com.yans.navobserver.room.entities

import androidx.room.ColumnInfo

data class LatLonAlt(
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "altitude")
    val altitude: Double
)