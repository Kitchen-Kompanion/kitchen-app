package com.example.kitchenkompanion

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object InventoryManager {
    private const val PREF_NAME = "inventory_prefs"
    private const val INVENTORY_KEY = "inventory_list"

    private lateinit var sharedPrefs: SharedPreferences
    private val gson = Gson()
    private var inventoryList: MutableList<InventoryItem> = mutableListOf()

    fun init(context: Context) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadInventory()
        val isInitialized = sharedPrefs.getBoolean("initialized", false)

        if (!isInitialized) {
            inventoryList.addAll(
                listOf(
                    InventoryItem("Apple", "Kyumin", "Fruits", 5, "2025-04-15"),
                    InventoryItem("Milk", "Kyumin", "Dairy", 2, "2025-04-10"),
                    InventoryItem("Bread", "Kyumin", "Pantry", 1, "2025-04-08"),
                    InventoryItem("Butter", "Kyumin", "Frozen", 3, "2025-04-20"),
                    InventoryItem("Chips", "Kyumin", "Snacks", 4, "2025-07-01"),
                    InventoryItem("Water", "Kyumin", "Beverages", 6, "2026-01-01"),
                    InventoryItem("Soda", "Kyumin", "Beverages", 6, "2026-05-01"),
                    InventoryItem("Tomato", "Kyumin", "Fruits", 1, "2025-04-03"),
                    InventoryItem("Yogurt", "Kyumin", "Dairy", 2, "2025-04-05"),
                    InventoryItem("Cheese", "Kyumin", "Dairy", 5, "2025-04-18"),
                    InventoryItem("Banana", "Kyumin", "Fruits", 1, "2025-04-02"),
                    InventoryItem("Frozen pizza", "Kyumin", "Frozen", 2, "2025-08-01"),
                    InventoryItem("Cereal", "Kyumin", "Pantry", 3, "2025-06-15"),
                    InventoryItem("Coffee", "Kyumin", "Beverages", 1, "2025-04-06")
                )
            )

            saveInventory()

            sharedPrefs.edit().putBoolean("initialized", true).apply()
        } else {

            loadInventory()
        }
    }

    private fun loadInventory() {
        val json = sharedPrefs.getString(INVENTORY_KEY, null)
        if (json != null) {
            val type = object : TypeToken<MutableList<InventoryItem>>() {}.type
            inventoryList = gson.fromJson(json, type)
        }
    }

    private fun saveInventory() {
        val json = gson.toJson(inventoryList)
        sharedPrefs.edit().putString(INVENTORY_KEY, json).apply()
    }
    fun saveItemsToPrefs(context: Context) {
        init(context)
        saveInventory()
    }
    fun addItem(item: InventoryItem) {
        inventoryList.add(item)
        saveInventory()
    }

    fun removeItem(item: InventoryItem) {
        inventoryList.remove(item)
        saveInventory()
    }

    fun getItems(): List<InventoryItem> {
        return inventoryList
    }

    fun clearAll() {
        inventoryList.clear()
        saveInventory()
        sharedPrefs.edit().clear().apply()
    }
}
