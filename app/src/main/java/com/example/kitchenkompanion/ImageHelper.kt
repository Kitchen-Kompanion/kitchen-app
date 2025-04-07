// 파일명: ImageHelper.kt
package com.example.kitchenkompanion

import com.example.kitchenkompanion.R

object ImageHelper {
    fun getImageResId(name: String): Int {
        return when (name.lowercase()) {
            "mango" -> R.drawable.mango
            "apple" -> R.drawable.apple
            "banana" -> R.drawable.banana
            "milk" -> R.drawable.milk
            "bread" -> R.drawable.bread
            else -> R.drawable.default_image
        }
    }
}
