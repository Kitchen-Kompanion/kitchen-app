package com.example.kitchenkompanion

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

object HomeManager {
    private const val PREF_NAME = "home_prefs"
    private const val EXPIRING_ITEMS_KEY = "expiring_items"
    private const val RECENT_ITEMS_KEY = "recent_items"
    private const val FAVORITES_KEY = "favorite_items"

    private lateinit var sharedPrefs: SharedPreferences
    private val gson = Gson()
    private var expiringItems: MutableList<FoodItem> = mutableListOf()
    private var recentItems: MutableList<FoodItem> = mutableListOf()
    private var favoriteItems: MutableList<String> = mutableListOf()

    data class FoodItem(
        val name: String,
        val expireDate: String,
        val type: String,
        val count: Int = 1
    )

    fun init(context: Context) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadItems()

        val isInitialized = sharedPrefs.getBoolean("home_initialized", false)
        if (!isInitialized) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val today = Calendar.getInstance()

            val items = listOf(
                FoodItem("Pear", getDateString(today, 2), "Fruits"),
                FoodItem("Apple", getDateString(today, 3), "Fruits"),
                FoodItem("Banana", getDateString(today, 1), "Fruits"),
                FoodItem("Orange", getDateString(today, 4), "Fruits"),
                FoodItem("Milk", getDateString(today, 2), "Dairy")
            )

            val recentItems = listOf(
                FoodItem("Melon", getDateString(today, 5), "Fruits"),
                FoodItem("Carrot", getDateString(today, 10), "Vegetables"),
                FoodItem("Mushroom", getDateString(today, 4), "Vegetables")
            )

            this.expiringItems.addAll(items)
            this.recentItems.addAll(recentItems)

            saveItems()

            sharedPrefs.edit().putBoolean("home_initialized", true).apply()
        }
    }

    private fun getDateString(calendar: Calendar, daysToAdd: Int): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = calendar.clone() as Calendar
        date.add(Calendar.DAY_OF_MONTH, daysToAdd)
        return dateFormat.format(date.time)
    }

    private fun loadItems() {
        val expiringJson = sharedPrefs.getString(EXPIRING_ITEMS_KEY, null)
        if (expiringJson != null) {
            val type = object : TypeToken<MutableList<FoodItem>>() {}.type
            expiringItems = gson.fromJson(expiringJson, type)
        }

        val recentJson = sharedPrefs.getString(RECENT_ITEMS_KEY, null)
        if (recentJson != null) {
            val type = object : TypeToken<MutableList<FoodItem>>() {}.type
            recentItems = gson.fromJson(recentJson, type)
        }

        val favoritesJson = sharedPrefs.getString(FAVORITES_KEY, null)
        if (favoritesJson != null) {
            val type = object : TypeToken<MutableList<String>>() {}.type
            favoriteItems = gson.fromJson(favoritesJson, type)
        }
    }

    private fun saveItems() {
        val expiringJson = gson.toJson(expiringItems)
        val recentJson = gson.toJson(recentItems)
        val favoritesJson = gson.toJson(favoriteItems)

        sharedPrefs.edit()
            .putString(EXPIRING_ITEMS_KEY, expiringJson)
            .putString(RECENT_ITEMS_KEY, recentJson)
            .putString(FAVORITES_KEY, favoritesJson)
            .apply()
    }

    fun getExpiringItems(): List<FoodItem> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return expiringItems.sortedBy {
            try {
                dateFormat.parse(it.expireDate)
            } catch (e: Exception) {
                Calendar.getInstance().time
            }
        }
    }

    fun getRecentItems(): List<FoodItem> {
        return recentItems
    }

    fun getFavoriteStatus(itemName: String): Boolean {
        return favoriteItems.contains(itemName)
    }

    fun toggleFavorite(itemName: String): Boolean {
        val newStatus = if (favoriteItems.contains(itemName)) {
            favoriteItems.remove(itemName)
            false
        } else {
            favoriteItems.add(itemName)
            true
        }
        saveItems()
        return newStatus
    }

    fun getDaysUntilExpiry(foodItem: FoodItem): Int {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val today = Calendar.getInstance().time
            val expireDate = dateFormat.parse(foodItem.expireDate)

            // Calculate days between dates
            val diff = expireDate.time - today.time
            return (diff / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            return 0
        }
    }

    fun getExpiringItemsFromInventory(): List<FoodItem> {
        return getExpiringItems()
    }

    fun saveItemsToPrefs(context: Context) {
        init(context)
        saveItems()
    }
}