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
    }
}
