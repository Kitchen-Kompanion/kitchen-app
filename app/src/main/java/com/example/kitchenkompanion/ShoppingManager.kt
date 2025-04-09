package com.example.kitchenkompanion

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ShoppingManager {
    private const val PREF_NAME = "shopping_prefs"
    private const val SHOPPING_KEY = "shopping_list"

    private lateinit var sharedPrefs: SharedPreferences
    private val gson = Gson()
    private var shoppingList: MutableList<ShoppingItem> = mutableListOf()

    fun init(context: Context) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadShoppingList()

        val isInitialized = sharedPrefs.getBoolean("shopping_initialized", false)
        if (!isInitialized) {
            shoppingList.addAll(
                listOf(
                    ShoppingItem("Watermelon", 3, 15.99, false),
                    ShoppingItem("Tomato", 3, 4.50, false),
                    ShoppingItem("Mango", 1, 3.99, false),
                    ShoppingItem("Banana", 3, 2.99, false),
                    ShoppingItem("Milk", 3, PriceHelper.getPrice("Milk"), false)
                )
            )
            saveShoppingList()

            sharedPrefs.edit().putBoolean("shopping_initialized", true).apply()
        }
    }

    private fun loadShoppingList() {
        val json = sharedPrefs.getString(SHOPPING_KEY, null)
        if (json != null) {
            val type = object : TypeToken<MutableList<ShoppingItem>>() {}.type
            shoppingList = gson.fromJson(json, type)
        }
    }

    private fun saveShoppingList() {
        val json = gson.toJson(shoppingList)
        sharedPrefs.edit().putString(SHOPPING_KEY, json).apply()
    }

    fun saveItemsToPrefs(context: Context) {
        init(context)
        saveShoppingList()
    }

    fun addItem(item: ShoppingItem) {
        shoppingList.add(item)
        saveShoppingList()
    }

    fun removeItem(item: ShoppingItem) {
        shoppingList.removeAll {
            it.name == item.name && it.price == item.price && it.quantity == item.quantity
        }
        saveShoppingList()
    }

    fun updateItem(index: Int, isChecked: Boolean) {
        if (index in shoppingList.indices) {
            shoppingList[index].checked = isChecked
            saveShoppingList()
        }
    }

    fun getItems(): List<ShoppingItem> {
        return shoppingList
    }

    fun getCheckedItems(): List<ShoppingItem> {
        return shoppingList.filter { it.checked }
    }

    fun removeCheckedItems() {
        shoppingList.removeAll { it.checked }
        saveShoppingList()
    }

    fun clearAll() {
        shoppingList.clear()
        saveShoppingList()
    }

    fun calculateTotalCheckedPrice(): Double {
        return shoppingList.filter { it.checked }.sumOf { it.price * it.quantity }
    }
}