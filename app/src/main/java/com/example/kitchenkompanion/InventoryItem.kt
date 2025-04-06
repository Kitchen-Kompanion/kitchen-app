package com.example.kitchenkompanion
data class InventoryItem(
    val name: String,
    val owner: String,
    val type: String,
    val count: Int,
    val expireDate: String
)