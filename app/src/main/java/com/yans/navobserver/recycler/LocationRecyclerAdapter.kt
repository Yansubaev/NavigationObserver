package com.yans.navobserver.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.yans.navobserver.databinding.ItemLocationInfoBinding
import com.yans.navobserver.room.entities.LocationInfo

class LocationRecyclerAdapter(
    private val context: Context
) : ListAdapter<LocationInfo, LocationViewHolder>(DiffUtilsCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder =
        LocationViewHolder(
            ItemLocationInfoBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.initView(getItem(position))
    }
}

class DiffUtilsCallback : DiffUtil.ItemCallback<LocationInfo>() {
    override fun areItemsTheSame(oldItem: LocationInfo, newItem: LocationInfo): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: LocationInfo, newItem: LocationInfo): Boolean =
        oldItem.contentEquals(newItem)
}