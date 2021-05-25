package com.yans.navobserver.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yans.navobserver.Constants
import com.yans.navobserver.databinding.ActivityMainBinding
import com.yans.navobserver.gps.LocationData
import com.yans.navobserver.recycler.LocationRecyclerAdapter
import com.yans.navobserver.room.NavObserverDatabase
import com.yans.navobserver.service.LocationService
import com.yans.navobserver.service.LocationViewModel
import com.yans.navobserver.service.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(NavObserverDatabase.getInstance(this))
        ).get(LocationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swtLocationService.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkPermissionsAndStartService()
            } else {
                stopLocationService()
            }
        }
        initRecycler()
        binding.btnClear.setOnClickListener {
            viewModel.clearInfos()
        }
        binding.btnStatistics.setOnClickListener {
            StatisticsFragment().show(
                supportFragmentManager,
                StatisticsFragment::class.java.simpleName
            )
        }
    }

    override fun onResume() {
        super.onResume()
        binding.swtLocationService.isChecked = LocationService.isRunning
    }

    private fun initRecycler() {
        val adapter = LocationRecyclerAdapter(this)
        binding.recLocations.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recLocations.adapter = adapter

        viewModel.locationData.observe(this) {
            if (it == null) {
                binding.laySwipeRefresh.isRefreshing = true
            } else {
                adapter.submitList(it)
                binding.laySwipeRefresh.isRefreshing = false
            }
        }

        binding.laySwipeRefresh.setOnRefreshListener {
            viewModel.fetchInfos()
        }
    }

    private fun checkPermissionsAndStartService() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    Constants.REQUEST_PERMISSION_LOCATION
                )
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    Constants.REQUEST_PERMISSION_LOCATION
                )
            }
        } else {
            startLocationService()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_PERMISSION_LOCATION && permissions.size > 1 &&
            permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
            permissions[1] == Manifest.permission.ACCESS_COARSE_LOCATION &&
            (
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                            || grantResults[1] == PackageManager.PERMISSION_GRANTED
                            || grantResults[2] == PackageManager.PERMISSION_GRANTED)
        ) {
            startLocationService()
        } else {
            binding.swtLocationService.isChecked = false
            stopLocationService()
        }
    }

    private fun startLocationService(forceStart: Boolean = false) {
        sendCommand(Constants.START_LOCATION_SERVICE)
        LocationData.locationData.observe(this) { l ->
            //Log.d(javaClass.simpleName, "Lat: ${l.latitude}, Lon: ${l.longitude}")
            viewModel.fetchInfos()
        }
    }

    private fun stopLocationService() {
        sendCommand(Constants.STOP_LOCATION_SERVICE)
        LocationData.locationData.removeObservers(this)
    }

    private fun sendCommand(command: String) {
        Intent(this, LocationService::class.java).apply {
            action = command
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(this)
            } else {
                startService(this)
            }
        }
    }
}