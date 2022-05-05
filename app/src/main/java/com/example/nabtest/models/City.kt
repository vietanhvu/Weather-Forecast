package com.example.nabtest.models

data class City(
    val id: Int,
    val name: String,
    val coord: Coordinator,
    val country: String,
    val population: Int,
    val timezone: Int
)