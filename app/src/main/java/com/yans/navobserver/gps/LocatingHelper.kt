package com.yans.navobserver.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat

object LocatingHelper : LocationListener {

    private var locationManager: LocationManager? = null
    private var locationUpdater: ((Location)->Unit)? = null

    override fun onLocationChanged(location: Location) {
        locationUpdater?.invoke(location)
    }

    fun startLocating(context: Context, locationUpdateListener: (Location)->Unit){
        locationUpdater = locationUpdateListener
        locationManager = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).also {
                locationManager ->
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                1F,
                this
            )
        }
    }

    fun stopLocating(){
        locationManager?.removeUpdates(this)
    }
}