package com.example.kitchenkompanion

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.util.Locale

class ShoppingFragment : Fragment() {

    // UI components
    private lateinit var tvSelectedItems: TextView
    private lateinit var tvCheckedItems: TextView
    private lateinit var etNewItem: EditText
    private lateinit var btnAddItem: Button
    private lateinit var btnClear: Button
    private lateinit var btnComplete: Button
    private lateinit var tvTotalPrice: TextView
    private lateinit var itemsContainer: ViewGroup

    // Checkboxes for initial items
    private lateinit var cbItem1: CheckBox
    private lateinit var cbItem2: CheckBox
    private lateinit var cbItem3: CheckBox
    private lateinit var cbItem4: CheckBox

    // Data
    private var totalItems = 10
    private var checkedItems = 0
    // Initialize shopping items with fixed prices
    private val shoppingItems = mutableListOf(
        ShoppingItem("Melon", 3, 15.99, false),
        ShoppingItem("Soup", 3, 4.50, false),
        ShoppingItem("Mango", 1, 3.99, false),
        ShoppingItem("Banana", 3, 2.99, false)
    )

    // Currency formatter
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shopping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        initViews(view)

        // Set up listeners
        setupListeners()

        // Initialize data
        updateUI()

        // Initialize estimated total to $0.00 since nothing is checked at start
        tvTotalPrice.text = currencyFormatter.format(0.0)
    }

    private fun initViews(view: View) {
        // Text views
        tvSelectedItems = view.findViewById(R.id.tv_selected_items)
        tvCheckedItems = view.findViewById(R.id.tv_checked_items)

        // Find the estimated total TextView in the summary section
        val linearLayouts = mutableListOf<ViewGroup>()
        findAllLinearLayouts(view, linearLayouts)

        // First try to find the summary card
        var summaryCard: ViewGroup? = null
        for (layout in linearLayouts) {
            for (i in 0 until layout.childCount) {
                val child = layout.getChildAt(i)
                if (child is TextView && child.text.toString().contains("Shopping Summary")) {
                    summaryCard = layout.parent as? ViewGroup
                    break
                }
            }
            if (summaryCard != null) break
        }

        // Find the total price TextView
        if (summaryCard != null) {
            for (i in 0 until summaryCard.childCount) {
                val child = summaryCard.getChildAt(i)
                if (child is LinearLayout) {
                    for (j in 0 until child.childCount) {
                        val innerChild = child.getChildAt(j)
                        if (innerChild is LinearLayout) {
                            val lastTextView = innerChild.getChildAt(innerChild.childCount - 1)
                            if (lastTextView is TextView && lastTextView.text.toString().contains("$")) {
                                val firstTextView = innerChild.getChildAt(0)
                                if (firstTextView is TextView &&
                                    firstTextView.text.toString().contains("Estimated total")) {
                                    tvTotalPrice = lastTextView
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }

        // Fallback if we couldn't find it
        if (!::tvTotalPrice.isInitialized) {
            tvTotalPrice = TextView(context)
        }

        // Find the container that holds all shopping items
        itemsContainer = findShoppingItemsContainer(view)

        // Input field and buttons
        etNewItem = view.findViewById(R.id.et_new_item)
        btnAddItem = view.findViewById(R.id.btn_add_item)
        btnClear = view.findViewById(R.id.btn_clear)
        btnComplete = view.findViewById(R.id.btn_complete)

        // Checkboxes
        cbItem1 = view.findViewById(R.id.cb_item1)
        cbItem2 = view.findViewById(R.id.cb_item2)
        cbItem3 = view.findViewById(R.id.cb_item3)
        cbItem4 = view.findViewById(R.id.cb_item4)
    }

    private fun setupListeners() {
        // Set checkbox listeners for initial items
        setupInitialItemListeners()

        // Add item button
        btnAddItem.setOnClickListener {
            addNewItem()
        }

        // Clear button - now will remove checked items
        btnClear.setOnClickListener {
            clearAllItems()
        }

        // Complete shopping button
        btnComplete.setOnClickListener {
            completeShoppingSession()
        }
    }

    private fun updateCheckedItems() {
        // Just update the count of checked items
        checkedItems = shoppingItems.count { it.checked }
        tvCheckedItems.text = checkedItems.toString()

        // Only update the estimated total, NOT the individual item prices
        updateEstimatedTotal()
    }

    private fun updateEstimatedTotal() {
        // Calculate total price based on what's checked only
        val totalPrice = shoppingItems.filter { it.checked }
            .sumOf { it.price * it.quantity }

        // Format and display the total price in the summary section only
        tvTotalPrice.text = currencyFormatter.format(totalPrice)
    }

    private fun addNewItem() {
        // Call the dynamic implementation
        addNewItemWithDynamicUI()
    }

    private fun addNewItemWithDynamicUI() {
        val itemName = etNewItem.text.toString().trim()

        if (itemName.isNotEmpty()) {
            // Create a new shopping item with default values
            val newItem = ShoppingItem(itemName, 1, 1.99, false)

            // Add to our data model
            shoppingItems.add(newItem)

            // Create a new row for the item dynamically
            val itemRow = createNewItemRow(newItem, shoppingItems.size - 1)

            // Add the new view to the container
            itemsContainer.addView(itemRow)

            // Clear the input field
            etNewItem.text.clear()

            // Update total items count
            totalItems++
            updateUI()

            // Show confirmation
            Toast.makeText(context, "Added: $itemName", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Please enter an item name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearAllItems() {
        // Create a list of items to remove (checked items)
        val itemsToRemove = shoppingItems.filter { it.checked }

        if (itemsToRemove.isEmpty()) {
            Toast.makeText(context, "No items selected to remove", Toast.LENGTH_SHORT).show()
            return
        }

        // Remove checked items from the data model
        shoppingItems.removeAll { it.checked }

        // Remove the corresponding views from the UI
        // We need to rebuild the UI since indexes will change
        rebuildItemsUI()

        // Update counts
        totalItems -= itemsToRemove.size
        checkedItems = 0

        // Update UI
        updateUI()

        // Reset estimated total to zero
        tvTotalPrice.text = currencyFormatter.format(0.0)

        Toast.makeText(context, "${itemsToRemove.size} item(s) removed", Toast.LENGTH_SHORT).show()
    }

    /**
     * Rebuilds the entire items UI after removing items
     */
    private fun rebuildItemsUI() {
        // First, clear all existing items from the container
        // Keep a reference to the "My Shopping List" TextView if it exists
        var titleTextView: TextView? = null
        for (i in 0 until itemsContainer.childCount) {
            val child = itemsContainer.getChildAt(i)
            if (child is TextView && child.text.toString().contains("My Shopping List")) {
                titleTextView = child
                break
            }
        }

        // Clear the container
        itemsContainer.removeAllViews()

        // Add back the title if we found it
        if (titleTextView != null) {
            itemsContainer.addView(titleTextView)
        }

        // Re-add all items in the updated list
        for (i in shoppingItems.indices) {
            val item = shoppingItems[i]
            val itemRow = createNewItemRow(item, i)
            itemsContainer.addView(itemRow)
        }

        // Update the checkbox references for the first four items if they still exist
        if (shoppingItems.size >= 1) {
            cbItem1 = findCheckboxInRow(itemsContainer.getChildAt(if (titleTextView != null) 1 else 0))
        }
        if (shoppingItems.size >= 2) {
            cbItem2 = findCheckboxInRow(itemsContainer.getChildAt(if (titleTextView != null) 2 else 1))
        }
        if (shoppingItems.size >= 3) {
            cbItem3 = findCheckboxInRow(itemsContainer.getChildAt(if (titleTextView != null) 3 else 2))
        }
        if (shoppingItems.size >= 4) {
            cbItem4 = findCheckboxInRow(itemsContainer.getChildAt(if (titleTextView != null) 4 else 3))
        }

        // Re-setup listeners for the initial items
        setupInitialItemListeners()
    }

    /**
     * Finds a checkbox within a row
     */
    private fun findCheckboxInRow(row: View?): CheckBox {
        if (row is ViewGroup) {
            for (i in 0 until row.childCount) {
                val child = row.getChildAt(i)
                if (child is CheckBox) {
                    return child
                }
            }
        }
        // Fallback to a new checkbox if not found
        return CheckBox(requireContext())
    }

    /**
     * Setup listeners just for the initial items
     */
    private fun setupInitialItemListeners() {
        // Only setup listeners if we have items and the checkboxes are initialized
        try {
            if (shoppingItems.size >= 1 && ::cbItem1.isInitialized) {
                cbItem1.setOnCheckedChangeListener { _, isChecked ->
                    shoppingItems[0].checked = isChecked
                    updateCheckedItems()
                }
            }

            if (shoppingItems.size >= 2 && ::cbItem2.isInitialized) {
                cbItem2.setOnCheckedChangeListener { _, isChecked ->
                    shoppingItems[1].checked = isChecked
                    updateCheckedItems()
                }
            }

            if (shoppingItems.size >= 3 && ::cbItem3.isInitialized) {
                cbItem3.setOnCheckedChangeListener { _, isChecked ->
                    shoppingItems[2].checked = isChecked
                    updateCheckedItems()
                }
            }

            if (shoppingItems.size >= 4 && ::cbItem4.isInitialized) {
                cbItem4.setOnCheckedChangeListener { _, isChecked ->
                    shoppingItems[3].checked = isChecked
                    updateCheckedItems()
                }
            }
        } catch (e: Exception) {
            // Ignore errors in case there's an index issue after removals
        }
    }

    private fun completeShoppingSession() {
        val checkedItemsCount = shoppingItems.count { it.checked }

        if (checkedItemsCount > 0) {
            if (checkedItemsCount == shoppingItems.size) {
                Toast.makeText(context, "Shopping completed! All items purchased.", Toast.LENGTH_LONG).show()

                // For demo purposes, navigate back if possible or just show the toast
                try {
                    // Use the newer API for navigating back (replacing deprecated onBackPressed)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    } else {
                        @Suppress("DEPRECATION")
                        requireActivity().onBackPressed()
                    }
                } catch (e: Exception) {
                    // If navigation fails, just show another toast
                    Toast.makeText(context, "Shopping session completed!", Toast.LENGTH_SHORT).show()
                }
            } else {
                val remainingItems = shoppingItems.size - checkedItemsCount
                Toast.makeText(context, "You still have $remainingItems items to purchase!", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "No items have been checked. Please select items you've purchased.", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI() {
        // Update item count display
        tvSelectedItems.text = getString(R.string.items_count_format, totalItems)
        tvCheckedItems.text = checkedItems.toString()
    }

    /**
     * Creates a new row layout for an item
     */
    private fun createNewItemRow(item: ShoppingItem, index: Int): LinearLayout {
        // Create the main row layout
        val rowLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
            setBackgroundColor(Color.parseColor("#EEEEEE"))

            val layoutParams = layoutParams as LinearLayout.LayoutParams
            layoutParams.bottomMargin = dpToPx(8)
        }

        // Add checkbox
        val checkbox = CheckBox(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnCheckedChangeListener { _, isChecked ->
                if (index < shoppingItems.size) {
                    shoppingItems[index].checked = isChecked
                    updateCheckedItems()
                }
            }
        }
        rowLayout.addView(checkbox)

        // Choose appropriate image based on item name
        val drawableName = when (item.name.toLowerCase()) {
            "melon" -> "melon"
            "soup" -> "soup1"
            "mango" -> "mango"
            "banana" -> "banana"
            else -> "" // For new items, handle separately
        }

        // Add image for the item
        val imageView = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(dpToPx(48), dpToPx(48)).apply {
                leftMargin = dpToPx(8)
            }

            // Try to use the appropriate image resource based on item name
            try {
                if (drawableName.isNotEmpty()) {
                    // Use the specific drawable for known items
                    val resourceId = resources.getIdentifier(drawableName, "drawable",
                        requireContext().packageName)

                    if (resourceId != 0) {
                        setImageResource(resourceId)
                    } else {
                        // Default icon if specific image isn't available
                        setImageResource(android.R.drawable.ic_menu_gallery)
                    }
                } else {
                    // For new items, use a default image
                    setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } catch (e: Exception) {
                // Fallback to system icon
                setImageResource(android.R.drawable.ic_menu_gallery)
            }

            contentDescription = "Image of ${item.name}"
        }
        rowLayout.addView(imageView)

        // Create inner LinearLayout for item details
        val detailsLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
                leftMargin = dpToPx(16)
            }
        }

        // Add item name
        val nameTextView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = item.name
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
        }
        detailsLayout.addView(nameTextView)

        // Add quantity
        val quantityTextView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = "Qty: ${item.quantity}"
            textSize = 14f
        }
        detailsLayout.addView(quantityTextView)

        // Add details layout to the row
        rowLayout.addView(detailsLayout)

        // Add price
        val priceTextView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = currencyFormatter.format(item.price)
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
        }
        rowLayout.addView(priceTextView)

        return rowLayout
    }

    /**
     * Finds the container where shopping items are displayed
     */
    private fun findShoppingItemsContainer(view: View): ViewGroup {
        // First we need to find the CardView that contains all shopping items
        val cards = mutableListOf<CardView>()
        findAllCardViews(view, cards)

        for (card in cards) {
            // Look through children to find "My Shopping List" TextView
            val linearLayouts = mutableListOf<LinearLayout>()
            findDirectLinearLayouts(card, linearLayouts)

            for (layout in linearLayouts) {
                // This should be the main container inside the card
                // Now look for the TextView that says "My Shopping List"
                for (i in 0 until layout.childCount) {
                    val child = layout.getChildAt(i)
                    if (child is TextView && child.text.toString().contains("My Shopping List")) {
                        // Found it! Return this layout as our container
                        return layout
                    }
                }
            }
        }

        // If we can't find the container, return a placeholder
        // This shouldn't happen with a properly structured layout
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
    }

    /**
     * Helper method to find all CardViews in the view hierarchy
     */
    private fun findAllCardViews(view: View, result: MutableList<CardView>) {
        if (view is ViewGroup) {
            if (view is CardView) {
                result.add(view)
            }
            for (i in 0 until view.childCount) {
                findAllCardViews(view.getChildAt(i), result)
            }
        }
    }

    /**
     * Helper method to find direct LinearLayout children of a ViewGroup
     */
    private fun findDirectLinearLayouts(parent: ViewGroup, result: MutableList<LinearLayout>) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is LinearLayout) {
                result.add(child)
            }
        }
    }

    /**
     * Helper method to find all LinearLayouts in the view hierarchy
     */
    private fun findAllLinearLayouts(view: View, result: MutableList<ViewGroup>) {
        if (view is ViewGroup) {
            if (view is LinearLayout) {
                result.add(view)
            }
            for (i in 0 until view.childCount) {
                findAllLinearLayouts(view.getChildAt(i), result)
            }
        }
    }

    /**
     * Utility to convert dp to pixels
     */
    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale).toInt()
    }

    // Data class for shopping items
    data class ShoppingItem(
        val name: String,
        val quantity: Int,
        val price: Double,
        var checked: Boolean
    )
}