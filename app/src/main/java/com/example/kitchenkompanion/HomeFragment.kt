package com.example.kitchenkompanion

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class HomeFragment : Fragment() {

    data class FoodItem(
        val name: String,
        val daysLeft: Int,
        val imageResId: Int,
        var isFavorite: Boolean = false
    )

    private lateinit var expiringItems: List<FoodItem>
    private lateinit var expiredItems: List<FoodItem>

    private var currentExpiringIndex = 0
    private var currentExpiredIndex = 0
    private val maxIndicators = 5

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.home, container, false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopButtons(view)
        loadItems(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        view?.let { loadItems(it) }
    }

    private fun setupTopButtons(view: View) {
        view.findViewById<ImageButton>(R.id.profileButton)?.setOnClickListener {
            Toast.makeText(context, R.string.profile_toast, Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageButton>(R.id.settingsButton)?.setOnClickListener {
            Toast.makeText(context, R.string.settings_toast, Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadItems(view: View) {
        InventoryManager.init(requireContext())
        val today = LocalDate.now()
        val allItems = InventoryManager.getItems()

        expiringItems = allItems.mapNotNull {
            try {
                val date = LocalDate.parse(it.expireDate)
                val days = ChronoUnit.DAYS.between(today, date).toInt()
                if (days >= 0) FoodItem(it.name, days, ImageHelper.getImageResId(it.name)) else null
            } catch (e: Exception) { null }
        }.sortedBy { it.daysLeft }.take(5)

        expiredItems = allItems.mapNotNull {
            try {
                val date = LocalDate.parse(it.expireDate)
                val days = ChronoUnit.DAYS.between(date, today).toInt()
                if (days > 0) FoodItem(it.name, -days, ImageHelper.getImageResId(it.name)) else null
            } catch (e: Exception) { null }
        }.sortedBy { it.daysLeft }

        updateExpiringDisplay(view)
        updateExpiredDisplay(view)
    }

    private fun updateExpiringDisplay(view: View) {
        if (expiringItems.isEmpty()) {
            view.findViewById<TextView>(R.id.expiringSoonItem)?.text = "No expiring items"
            view.findViewById<TextView>(R.id.expiringSoonDays)?.text = "All fresh!"
            view.findViewById<ImageView>(R.id.expiringSoonImage)?.setImageResource(R.drawable.default_image)
            view.findViewById<ImageButton>(R.id.nextItemButton)?.isEnabled = false
            return
        }

        val item = expiringItems[currentExpiringIndex]
        view.findViewById<TextView>(R.id.expiringSoonItem)?.text = item.name
        view.findViewById<TextView>(R.id.expiringSoonDays)?.text =
            if (item.daysLeft == 0) "Expires Today" else "${item.daysLeft} days left"
        view.findViewById<ImageView>(R.id.expiringSoonImage)?.setImageResource(item.imageResId)

        view.findViewById<ImageButton>(R.id.nextItemButton)?.apply {
            isEnabled = expiringItems.size > 1
            setOnClickListener {
                currentExpiringIndex = (currentExpiringIndex + 1) % expiringItems.size
                updateExpiringDisplay(view)
            }
        }

        updateDots(view, R.id.indicatorContainer, currentExpiringIndex, expiringItems.size)
    }

    private fun updateExpiredDisplay(view: View) {
        if (expiredItems.isEmpty()) {
            view.findViewById<TextView>(R.id.expiredSoonItem)?.text = "No expired items"
            view.findViewById<TextView>(R.id.expiredSoonDays)?.text = "You're doing great!"
            view.findViewById<ImageView>(R.id.expiredSoonImage)?.setImageResource(R.drawable.default_image)
            view.findViewById<ImageButton>(R.id.expiredNextItemButton)?.isEnabled = false
            return
        }

        val item = expiredItems[currentExpiredIndex]
        view.findViewById<TextView>(R.id.expiredSoonItem)?.text = item.name
        view.findViewById<TextView>(R.id.expiredSoonDays)?.text = "${-item.daysLeft} days ago"
        view.findViewById<ImageView>(R.id.expiredSoonImage)?.setImageResource(item.imageResId)

        view.findViewById<ImageButton>(R.id.expiredNextItemButton)?.apply {
            isEnabled = expiredItems.size > 1
            setOnClickListener {
                currentExpiredIndex = (currentExpiredIndex + 1) % expiredItems.size
                updateExpiredDisplay(view)
            }
        }

        updateDots(view, R.id.expiredIndicatorContainer, currentExpiredIndex, expiredItems.size)
    }

    private fun updateDots(view: View, containerId: Int, currentIndex: Int, total: Int) {
        val container = view.findViewById<LinearLayout>(containerId) ?: return
        container.removeAllViews()
        val count = minOf(total, maxIndicators)
        val density = resources.displayMetrics.density
        val size = (8 * density).toInt()
        val margin = (2 * density).toInt()
        repeat(count) { i ->
            val dot = View(context).apply {
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    setMargins(margin, 0, margin, 0)
                }
                setBackgroundColor(if (i == currentIndex) Color.BLACK else Color.GRAY)
            }
            container.addView(dot)
        }
    }
}
