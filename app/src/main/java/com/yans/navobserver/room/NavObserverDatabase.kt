package com.yans.navobserver.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yans.navobserver.room.dao.LocationInfoDao
import com.yans.navobserver.room.entities.LatLonAlt
import com.yans.navobserver.room.entities.LocationInfo

@Database(
    entities = [
        LocationInfo::class
    ],
    version = 1
)
abstract class NavObserverDatabase : RoomDatabase() {
    abstract val locationInfoDao: LocationInfoDao

    companion object {
        private const val DATABASE_NAME = "navigation_observer_database"

        @Volatile
        private var INSTANCE: NavObserverDatabase? = null

        fun getInstance(context: Context): NavObserverDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NavObserverDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }

}