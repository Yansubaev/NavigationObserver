package com.yans.navobserver.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yans.navobserver.room.NavObserverDatabase
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val db: NavObserverDatabase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LocationViewModel::class.java) -> LocationViewModel(
                ServiceRepository(db.locationInfoDao)
            ) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }

}