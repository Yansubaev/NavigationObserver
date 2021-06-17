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
import com.yans.navobserver.service.LocationViewModel
import com.yans.navobserver.service.ViewModelFactory

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

        viewModel.distance.observe(this) { dist ->
            if (dist == null) {
                notEnoughInfo()
            } else {
                if (dist == 0.0) {
                    notEnoughInfo()
                } else {
                    binding.txtDistanceResult.text = dist.format(3)
                }
            }
        }
        viewModel.speed.observe(this) { speed ->
            if (speed == null) {
                notEnoughInfo()
            } else {
                if (speed == 0.0) {
                    notEnoughInfo()
                } else {
                    binding.txtDistanceResult.text = speed.format(2)
                }
            }
        }

        viewModel.calculateDistance()
        return binding.root
    }

    private fun notEnoughInfo() {
        binding.txtDistanceResult.text = getString(R.string.not_enough_information)
        binding.txtSpeedResult.text = getString(R.string.not_enough_information)
    }

}