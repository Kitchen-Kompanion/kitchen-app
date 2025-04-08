package com.example.kitchenkompanion

import java.io.Serializable

data class ShoppingItem(
    val name: String,
    val quantity: Int,
    val price: Double,
    var checked: Boolean
): Serializable