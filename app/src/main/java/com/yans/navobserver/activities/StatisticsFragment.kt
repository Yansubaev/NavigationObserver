package com.yans.navobserver.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yans.navobserver.R
import com.yans.navobserver.databinding.FragmentStatisticsBinding
import com.yans.navobserver.format
import com.yans.navobserver.room.NavObserverDatabase
import com.yans.navobserver.room.entities.LocationInfo
import com.yans.navobserver.service.LocationViewModel
import com.yans.navobserver.service.ViewModelFactory
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class StatisticsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentStatisticsBinding

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(NavObserverDatabase.getInstance(requireContext()))
        ).get(LocationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        viewModel.fetchInfos()
        viewModel.locationData.observe(this) {
            if (it.isNullOrEmpty()) {
                notEnoughInfo()
            } else {
                if(it.size > 1){
                    calculateDistance(it)
                } else {
                    notEnoughInfo()
                }
            }
        }

        return binding.root
    }

    private fun notEnoughInfo(){
        binding.txtDistanceResult.text = getString(R.string.not_enough_information)
        binding.txtSpeedResult.text = getString(R.string.not_enough_information)
    }

    private fun calculateDistance(locations: List<LocationInfo>) {
        var result = 0.0
        var speeds = 0.0
        for (i in 1 until locations.size) {
            val dist = getDistanceFromLatLonInKm(
                locations[i - 1].latitude,
                locations[i - 1].longitude,
                locations[i].latitude,
                locations[i].longitude
            )
            result += dist
            var time = (locations[i].time - locations[i - 1].time).toDouble()
            time /= (1000 * 60 * 60)
            speeds += dist / time
        }
        binding.txtDistanceResult.text = result.format(3)
        binding.txtSpeedResult.text = (speeds / (locations.size - 1)).format(2)
    }

    private fun getDistanceFromLatLonInKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val radius = 6371
        val dLat = deg2rad(lat2 - lat1)
        val dLon = deg2rad(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(deg2rad(lat1)) * cos(deg2rad(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }

    private fun deg2rad(deg: Double): Double {
        return deg * (Math.PI / 180)
    }
}