package com.example.kitchenkompanion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class HomeFragment : Fragment() {

    // Food data models
    data class FoodItem(
        val name: String,
        val daysLeft: Int,
        val imageResId: Int,
        var isFavorite: Boolean = false
    )

    // Sample data - replace with your database implementation
    private val expiringItems = listOf(
        FoodItem("Pear", 2, R.drawable.default_image),
        FoodItem("Apple", 3, R.drawable.apple),
        FoodItem("Banana", 1, R.drawable.banana),
        FoodItem("Orange", 4, R.drawable.orange),
        FoodItem("Milk", 2, R.drawable.milk)
    )

    private val recentItems = listOf(
        FoodItem("Melon", 5, R.drawable.melon),
        FoodItem("Carrot", 10, R.drawable.carrot),
        FoodItem("Mushroom", 4, R.drawable.default_image)
    )

    private var currentExpiringItemIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup UI components
        setupTopButtons(view)
        setupExpiringItemsCarousel(view)
        setupFavoriteButtons(view)
    }

    private fun setupTopButtons(view: View) {
        view.findViewById<ImageButton>(R.id.profileButton)?.setOnClickListener {
            Toast.makeText(context, "Profile", Toast.LENGTH_SHORT).show()
            // Navigate to profile screen
        }

        view.findViewById<ImageButton>(R.id.settingsButton)?.setOnClickListener {
            Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
            // Navigate to settings screen
        }
    }

    private fun setupExpiringItemsCarousel(view: View) {
        // Display the first expiring item
        updateExpiringItemDisplay(view)

        // Setup next button for carousel
        view.findViewById<ImageButton>(R.id.nextItemButton)?.setOnClickListener {
            currentExpiringItemIndex = (currentExpiringItemIndex + 1) % expiringItems.size
            updateExpiringItemDisplay(view)
        }
    }

    private fun updateExpiringItemDisplay(view: View) {
        val currentItem = expiringItems[currentExpiringItemIndex]

        // Update the UI with current item's details
        view.findViewById<TextView>(R.id.expiringSoonItem)?.text = currentItem.name
        view.findViewById<TextView>(R.id.expiringSoonDays)?.text = "${currentItem.daysLeft} Days Left"
        view.findViewById<ImageView>(R.id.expiringSoonImage)?.setImageResource(currentItem.imageResId)
    }

    private fun setupFavoriteButtons(view: View) {
        // Set up favorite buttons
        view.findViewById<ImageButton>(R.id.favoriteItem1)?.setOnClickListener {
            toggleFavorite(0, it as ImageButton)
        }

        view.findViewById<ImageButton>(R.id.favoriteItem2)?.setOnClickListener {
            toggleFavorite(1, it as ImageButton)
        }

        view.findViewById<ImageButton>(R.id.favoriteItem3)?.setOnClickListener {
            toggleFavorite(2, it as ImageButton)
        }
    }

    private fun toggleFavorite(itemIndex: Int, button: ImageButton) {
        // Toggle favorite status
        if (itemIndex < recentItems.size) {
            recentItems[itemIndex].isFavorite = !recentItems[itemIndex].isFavorite

            // Update button appearance using system icons
            if (recentItems[itemIndex].isFavorite) {
                button.setImageResource(android.R.drawable.btn_star_big_on)
                Toast.makeText(context, "${recentItems[itemIndex].name} added to favorites", Toast.LENGTH_SHORT).show()
            } else {
                button.setImageResource(android.R.drawable.btn_star)
                Toast.makeText(context, "${recentItems[itemIndex].name} removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }
}