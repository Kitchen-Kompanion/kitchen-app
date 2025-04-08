package com.example.kitchenkompanion

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
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

    // The app uses a fixed set of indicator dots
    private val maxIndicators = 5

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
        setupRecentItemsList(view)
    }

    private fun setupTopButtons(view: View) {
        val profileButton = view.findViewById<ImageButton>(R.id.profileButton)
        profileButton?.setOnClickListener {
            Toast.makeText(context, R.string.profile_toast, Toast.LENGTH_SHORT).show()
            // Navigate to profile screen
        }

        val settingsButton = view.findViewById<ImageButton>(R.id.settingsButton)
        settingsButton?.setOnClickListener {
            Toast.makeText(context, R.string.settings_toast, Toast.LENGTH_SHORT).show()
            // Navigate to settings screen
        }
    }

    private fun setupExpiringItemsCarousel(view: View) {
        // Check if list is empty to prevent crashes
        if (expiringItems.isEmpty()) {
            val itemNameTextView = view.findViewById<TextView>(R.id.expiringSoonItem)
            itemNameTextView?.setText(R.string.no_expiring_items)

            val daysTextView = view.findViewById<TextView>(R.id.expiringSoonDays)
            daysTextView?.setText(R.string.add_items_to_track)

            val nextButton = view.findViewById<ImageButton>(R.id.nextItemButton)
            nextButton?.isEnabled = false
            return
        }

        // Display the first expiring item
        updateExpiringItemDisplay(view)

        // Setup next button for carousel
        val nextButton = view.findViewById<ImageButton>(R.id.nextItemButton)
        nextButton?.setOnClickListener {
            currentExpiringItemIndex = (currentExpiringItemIndex + 1) % expiringItems.size
            updateExpiringItemDisplay(view)
        }
    }

    private fun updateExpiringItemDisplay(view: View) {
        val currentItem = expiringItems[currentExpiringItemIndex]

        // Update the UI with current item's details
        val itemNameTextView = view.findViewById<TextView>(R.id.expiringSoonItem)
        itemNameTextView?.text = currentItem.name

        // Format days text based on days left
        val daysText = when (currentItem.daysLeft) {
            0 -> getString(R.string.expires_today)
            1 -> getString(R.string.one_day_left)
            else -> getString(R.string.days_left_format, currentItem.daysLeft)
        }

        val daysTextView = view.findViewById<TextView>(R.id.expiringSoonDays)
        daysTextView?.text = daysText

        // Set image or default if resource not found
        val imageView = view.findViewById<ImageView>(R.id.expiringSoonImage)
        try {
            imageView?.setImageResource(currentItem.imageResId)
        } catch (e: Exception) {
            imageView?.setImageResource(R.drawable.default_image)
        }

        // Update indicator dots
        updateIndicatorDots(view)
    }

    private fun updateIndicatorDots(view: View) {
        // Get container view that holds the indicators
        val container = view.findViewById<LinearLayout>(R.id.indicatorContainer) ?: return

        // Clear existing indicators if any
        container.removeAllViews()

        // Create new indicators based on number of items (max 5)
        val totalDots = minOf(expiringItems.size, maxIndicators)

        // Define dot sizes and colors directly to avoid resource dependencies
        val dotSize = 8 // 8dp
        val dotMargin = 2 // 2dp
        val activeColor = Color.BLACK
        val inactiveColor = Color.GRAY

        for (i in 0 until totalDots) {
            val dotView = View(context)

            // Convert dp to pixels
            val density = resources.displayMetrics.density
            val sizePx = (dotSize * density).toInt()
            val marginPx = (dotMargin * density).toInt()

            val params = LinearLayout.LayoutParams(sizePx, sizePx)
            params.setMargins(marginPx, 0, marginPx, 0)
            dotView.layoutParams = params

            // Set active/inactive state
            if (i == currentExpiringItemIndex) {
                dotView.setBackgroundColor(activeColor)
            } else {
                dotView.setBackgroundColor(inactiveColor)
            }

            container.addView(dotView)
        }
    }

    private fun setupRecentItemsList(view: View) {
        // Check if list is empty
        if (recentItems.isEmpty()) {
            // Hide the section or show empty state
            val recentItemsCard = view.findViewById<View>(R.id.recentItemsCard)
            recentItemsCard?.visibility = View.GONE
            return
        }

        // Initialize favorite buttons with correct state
        setupFavoriteButtons(view)
    }

    private fun setupFavoriteButtons(view: View) {
        // Set up favorite buttons with initial states
        val favoriteButtons = listOf(
            view.findViewById<ImageButton>(R.id.favoriteItem1),
            view.findViewById<ImageButton>(R.id.favoriteItem2),
            view.findViewById<ImageButton>(R.id.favoriteItem3)
        )

        // Initialize buttons with correct favorite state
        for (i in recentItems.indices) {
            if (i < favoriteButtons.size) {
                val button = favoriteButtons[i]

                // Set initial button state
                if (recentItems[i].isFavorite) {
                    button?.setImageResource(android.R.drawable.btn_star_big_on)
                } else {
                    button?.setImageResource(android.R.drawable.btn_star)
                }

                // Set click listener
                button?.setOnClickListener {
                    toggleFavorite(i, it as ImageButton)
                }
            }
        }
    }

    private fun toggleFavorite(itemIndex: Int, button: ImageButton) {
        // Make sure index is valid
        if (itemIndex < recentItems.size) {
            // Toggle favorite status
            recentItems[itemIndex].isFavorite = !recentItems[itemIndex].isFavorite

            // Update button appearance using system icons
            if (recentItems[itemIndex].isFavorite) {
                button.setImageResource(android.R.drawable.btn_star_big_on)
                Toast.makeText(context, getString(R.string.added_to_favorites, recentItems[itemIndex].name), Toast.LENGTH_SHORT).show()
            } else {
                button.setImageResource(android.R.drawable.btn_star)
                Toast.makeText(context, getString(R.string.removed_from_favorites, recentItems[itemIndex].name), Toast.LENGTH_SHORT).show()
            }

            // Here you would update the database with the new favorite status
        }
    }
}