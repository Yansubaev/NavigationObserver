package com.yans.navobserver.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.yans.navobserver.Constants
import com.yans.navobserver.R
import com.yans.navobserver.gps.LocatingHelper
import com.yans.navobserver.gps.LocationData
import com.yans.navobserver.room.NavObserverDatabase
import kotlinx.coroutines.launch
import java.util.*

class LocationService : LifecycleService() {

    private lateinit var repository: ServiceRepository

    companion object {
        var isRunning: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        repository =
            ServiceRepository(NavObserverDatabase.getInstance(applicationContext).locationInfoDao)
    }

    private fun locationChanged(l: Location) {
        lifecycleScope.launch {
            repository.insertInfo(l, Calendar.getInstance().timeInMillis)
        }
        //LocationData.locationData.postValue(l)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            when (action) {
                Constants.START_LOCATION_SERVICE -> {
                    activateService(intent)
                }
                Constants.STOP_LOCATION_SERVICE -> {
                    deactivateService(intent)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun activateService(intent: Intent) {
        isRunning = true
        LocatingHelper.startLocating(applicationContext, ::locationChanged)
        val channelID = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()
        else ""

        val notificationBuilder = NotificationCompat.Builder(
            this, channelID
        )
        val notification = notificationBuilder
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setContentText(getString(R.string.collecting_data))
            .build()

        startForeground(Constants.FOREGROUND_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelName = "Location Service"
        val channel = NotificationChannel(
            Constants.LOCATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
        return Constants.LOCATION_CHANNEL_ID
    }

    private fun deactivateService(intent: Intent) {
        isRunning = false
        LocatingHelper.stopLocating()
        stopService(intent)
    }
}