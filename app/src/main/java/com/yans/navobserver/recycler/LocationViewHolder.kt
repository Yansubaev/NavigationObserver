package com.yans.navobserver.recycler

import androidx.recyclerview.widget.RecyclerView
import com.yans.navobserver.databinding.ItemLocationInfoBinding
import com.yans.navobserver.room.entities.LocationInfo
import java.text.SimpleDateFormat
import java.util.*

class LocationViewHolder(
    private val binding: ItemLocationInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    fun initView(model: LocationInfo){
        binding.txtLatitude.text = model.latitude.toString()
        binding.txtLongitude.text = model.longitude.toString()
        binding.txtDate.text = SimpleDateFormat("dd-MM-yyyy").format(Date(model.time));
    }
}