package com.example.nabtest.utils

import com.example.nabtest.models.Temp
import kotlin.math.roundToInt

fun Temp.averageTemp(): Int = ((this.max + this.min) / 2).roundToInt()