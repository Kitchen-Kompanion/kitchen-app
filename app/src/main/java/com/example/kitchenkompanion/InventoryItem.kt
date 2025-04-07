package com.example.kitchenkompanion

import java.io.Serializable

data class InventoryItem(
    var name: String,
    var owner: String,
    var type: String,
    var count: Int,
    var expireDate: String
): Serializable