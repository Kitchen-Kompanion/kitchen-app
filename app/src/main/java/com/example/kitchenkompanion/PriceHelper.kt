package com.example.kitchenkompanion

object PriceHelper {
    private val priceMap = mapOf(
        "watermelon" to 15.99,
        "tomato" to 4.50,
        "mango" to 3.99,
        "banana" to 2.99,
        "apple" to 3.49,
        "grapes" to 5.99,
        "orange" to 4.29,
        "milk" to 2.19,
        "cheese" to 4.89,
        "bread" to 2.50,
        "cereal" to 3.75,
        "juice" to 2.89,
        "soda" to 1.99,
        "egg" to 2.79,
        "rice" to 5.49,
        "potato" to 1.99,
        "onion" to 1.49,
        "carrot" to 1.79,
        "yogurt" to 1.25,
        // ... Add more in here
    )

    fun getPrice(itemName: String): Double {
        return priceMap[itemName.lowercase()] ?: 1.99
    }
}
