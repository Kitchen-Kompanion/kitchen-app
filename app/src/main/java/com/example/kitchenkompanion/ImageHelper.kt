// 파일명: ImageHelper.kt
package com.example.kitchenkompanion

import com.example.kitchenkompanion.R

object ImageHelper {
    fun getImageResId(name: String): Int {
        return when (name.lowercase()) {
            "mango" -> R.drawable.mango
            "apple" -> R.drawable.apple
            "banana" -> R.drawable.banana
            "grapes" -> R.drawable.grape
            "orange" -> R.drawable.orange
            "strawberry" -> R.drawable.strawberry
            "watermelon" -> R.drawable.watermelon
            "carrot" -> R.drawable.carrot
            "potato" -> R.drawable.potato
            "onion" -> R.drawable.onion
            "lettuce" -> R.drawable.lettuce
            "broccoli" -> R.drawable.broccoli
            "cucumber" -> R.drawable.cucumber
            "tomato" -> R.drawable.tomato
            "milk" -> R.drawable.milk
            "cheese" -> R.drawable.cheese
            "yogurt" -> R.drawable.yogurt
            "butter" -> R.drawable.butter
            "cream" -> R.drawable.cream
            "bread" -> R.drawable.bread
            "rice" -> R.drawable.rice
            "pasta" -> R.drawable.pasta
            "cereal" -> R.drawable.cereal
            "flour" -> R.drawable.flour
            "oil" -> R.drawable.oil
            "salt" -> R.drawable.salt
            "sugar" -> R.drawable.sugar
            "egg" -> R.drawable.egg
            "frozen pizza" -> R.drawable.pizza
            "juice" -> R.drawable.juice
            "soda" -> R.drawable.soda
            "water" -> R.drawable.water
            "coffee" -> R.drawable.coffee
            "tea" -> R.drawable.tea
            "chips" -> R.drawable.chips
            "chocolate" -> R.drawable.chocolate
            "candy" -> R.drawable.candy
            "cookies" -> R.drawable.cookies
            else -> R.drawable.default_image
        }
    }
}

