package com.yans.navobserver

fun Double.format(digits: Int) = "%.${digits}f".format(this)