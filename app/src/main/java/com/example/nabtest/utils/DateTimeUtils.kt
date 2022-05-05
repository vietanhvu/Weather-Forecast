package com.example.nabtest.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.convertToDate(): String {
    val date = Date(this)
    val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy")
    return dateFormat.format(date)
}