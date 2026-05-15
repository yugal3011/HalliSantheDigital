package com.hallisanthe.digital.models

import java.io.Serializable

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
) : Serializable
