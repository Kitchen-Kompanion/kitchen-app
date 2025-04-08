package com.example.kitchenkompanion

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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

    private var currentExpiringItemIndex = 0
    private val maxIndicators = 5
    private lateinit var expiringItems: List<HomeManager.FoodItem>
    private lateinit var recentItems: List<HomeManager.FoodItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            // Initialize HomeManager
            HomeManager.init(requireContext())

            // Load data
            expiringItems = HomeManager.getExpiringItems()
            recentItems = HomeManager.getRecentItems()

            // Set text from resources to views
            view.findViewById<TextView>(R.id.appTitle).text = resources.getString(R.string.app_title)
            view.findViewById<TextView>(R.id.expiringSoonTitle).text = resources.getString(R.string.expiring_soon)
            view.findViewById<TextView>(R.id.recentlyAddedTitle).text = resources.getString(R.string.recently_added)

            // Set content descriptions
            view.findViewById<ImageButton>(R.id.profileButton).contentDescription = resources.getString(R.string.profile)
            view.findViewById<ImageButton>(R.id.settingsButton).contentDescription = resources.getString(R.string.settings)
            view.findViewById<ImageButton>(R.id.nextItemButton).contentDescription = resources.getString(R.string.next_item)
            view.findViewById<ImageView>(R.id.expiringSoonImage).contentDescription = resources.getString(R.string.expiring_food_image)

            for (i in 1..3) {
                val favoriteButton = when (i) {
                    1 -> view.findViewById<ImageButton>(R.id.favoriteItem1)
                    2 -> view.findViewById<ImageButton>(R.id.favoriteItem2)
                    3 -> view.findViewById<ImageButton>(R.id.favoriteItem3)
                    else -> null
                }
                favoriteButton?.contentDescription = resources.getString(R.string.favorite)
            }

            // Setup UI components
            setupTopButtons(view)
            setupExpiringItemsCarousel(view)
            setupRecentItemsList(view)
        } catch (e: Exception) {
            // Handle any initialization errors
            Log.e("HomeFragment", "Error initializing: ${e.message}", e)
            Toast.makeText(context, "Error initializing: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupTopButtons(view: View) {
        val profileButton = view.findViewById<ImageButton>(R.id.profileButton)
        profileButton?.setOnClickListener {
            Toast.makeText(context, resources.getString(R.string.profile_toast), Toast.LENGTH_SHORT).show()
            // Navigate to profile screen
        }

        val settingsButton = view.findViewById<ImageButton>(R.id.settingsButton)
        settingsButton?.setOnClickListener {
            Toast.makeText(context, resources.getString(R.string.settings_toast), Toast.LENGTH_SHORT).show()
            // Navigate to settings screen
        }
    }

    private fun setupExpiringItemsCarousel(view: View) {
        // Check if list is empty to prevent crashes
        if (expiringItems.isEmpty()) {
            val itemNameTextView = view.findViewById<TextView>(R.id.expiringSoonItem)
            itemNameTextView?.text = resources.getString(R.string.no_expiring_items)

            val daysTextView = view.findViewById<TextView>(R.id.expiringSoonDays)
            daysTextView?.text = resources.getString(R.string.add_items_to_track)

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
        val daysLeft = HomeManager.getDaysUntilExpiry(currentItem)

        // Update the UI with current item's details
        val itemNameTextView = view.findViewById<TextView>(R.id.expiringSoonItem)
        itemNameTextView?.text = currentItem.name

        // Format days text based on days left
        val daysText = when (daysLeft) {
            0 -> resources.getString(R.string.expires_today)
            1 -> resources.getString(R.string.one_day_left)
            else -> resources.getString(R.string.days_left_format, daysLeft)
        }

        val daysTextView = view.findViewById<TextView>(R.id.expiringSoonDays)
        daysTextView?.text = daysText

        // Set image
        val imageView = view.findViewById<ImageView>(R.id.expiringSoonImage)
        try {
            val imageResId = ImageHelper.getImageResId(currentItem.name)
            imageView?.setImageResource(imageResId)
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

        // Set up recent items display
        setupRecentItemsDisplay(view)
    }

    private fun setupRecentItemsDisplay(view: View) {
        // Find the container that will hold recent items
        val itemContainers = listOf(
            view.findViewById<View>(R.id.recentItem1Container),
            view.findViewById<View>(R.id.recentItem2Container),
            view.findViewById<View>(R.id.recentItem3Container)
        )

        // Set up favorite buttons with initial states
        val favoriteButtons = listOf(
            view.findViewById<ImageButton>(R.id.favoriteItem1),
            view.findViewById<ImageButton>(R.id.favoriteItem2),
            view.findViewById<ImageButton>(R.id.favoriteItem3)
        )

        // Set up item images
        val itemImages = listOf(
            view.findViewById<ImageView>(R.id.recentItem1Image),
            view.findViewById<ImageView>(R.id.recentItem2Image),
            view.findViewById<ImageView>(R.id.recentItem3Image)
        )

        // Set up item text views
        val itemTextViews = listOf(
            view.findViewById<TextView>(R.id.recentItem1Text),
            view.findViewById<TextView>(R.id.recentItem2Text),
            view.findViewById<TextView>(R.id.recentItem3Text)
        )

        // Display recent items (up to 3)
        for (i in recentItems.indices) {
            if (i >= 3) break // Only show up to 3 items

            val item = recentItems[i]
            val daysLeft = HomeManager.getDaysUntilExpiry(item)

            // Set item text
            val itemText = "${item.name} - ${
                when (daysLeft) {
                    0 -> resources.getString(R.string.expires_today)
                    1 -> resources.getString(R.string.one_day_left)
                    else -> resources.getString(R.string.days_left_format, daysLeft)
                }
            }"
            itemTextViews[i].text = itemText

            // Set content description for image
            itemImages[i].contentDescription = "${item.name} ${resources.getString(R.string.expiring_food_image)}"

            // Set item image
            try {
                val imageResId = ImageHelper.getImageResId(item.name)
                itemImages[i].setImageResource(imageResId)
            } catch (e: Exception) {
                itemImages[i].setImageResource(R.drawable.default_image)
            }

            // Set favorite button state
            val isFavorite = HomeManager.getFavoriteStatus(item.name)
            favoriteButtons[i].setImageResource(
                if (isFavorite) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star
            )

            // Set click listener for favorite button
            val index = i // Capture the index in a local variable
            favoriteButtons[i].setOnClickListener {
                toggleFavorite(index)
            }
        }

        // Hide unused items
        for (i in recentItems.size until 3) {
            itemContainers[i].visibility = View.GONE
        }
    }

    private fun toggleFavorite(itemIndex: Int) {
        // Make sure index is valid
        if (itemIndex < recentItems.size) {
            val item = recentItems[itemIndex]
            val newStatus = HomeManager.toggleFavorite(item.name)

            // Update button appearance
            val button = when (itemIndex) {
                0 -> view?.findViewById<ImageButton>(R.id.favoriteItem1)
                1 -> view?.findViewById<ImageButton>(R.id.favoriteItem2)
                2 -> view?.findViewById<ImageButton>(R.id.favoriteItem3)
                else -> null
            }

            button?.setImageResource(
                if (newStatus) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star
            )

            // Show appropriate toast message
            val message = if (newStatus) {
                resources.getString(R.string.added_to_favorites, item.name)
            } else {
                resources.getString(R.string.removed_from_favorites, item.name)
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data from HomeManager when fragment resumes
        try {
            expiringItems = HomeManager.getExpiringItems()
            recentItems = HomeManager.getRecentItems()
            view?.let { view ->
                setupExpiringItemsCarousel(view)
                setupRecentItemsList(view)
            }
        } catch (e: Exception) {
            // Handle any refresh errors
            Log.e("HomeFragment", "Error refreshing: ${e.message}", e)
        }
    }
}