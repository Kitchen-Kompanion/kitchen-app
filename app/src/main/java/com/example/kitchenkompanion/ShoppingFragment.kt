package com.example.kitchenkompanion

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.util.*

class ShoppingFragment : Fragment() {

    private lateinit var tvSelectedItems: TextView
    private lateinit var tvCheckedItems: TextView
    private lateinit var tvTotalItems: TextView
    private lateinit var etNewItem: EditText
    private lateinit var etQuantity: EditText
    private lateinit var btnAddItem: Button
    private lateinit var btnClear: Button
    private lateinit var btnComplete: Button
    private lateinit var tvTotalPrice: TextView
    private lateinit var itemsContainer: ViewGroup

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.shopping, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ShoppingManager.init(requireContext())
        initViews(view)
        setupListeners()
        refreshShoppingList()
    }

    private fun initViews(view: View) {
        tvSelectedItems = view.findViewById(R.id.tv_selected_items)
        tvCheckedItems = view.findViewById(R.id.tv_checked_items)
        tvTotalItems = view.findViewById(R.id.tv_total_items)
        etNewItem = view.findViewById(R.id.et_new_item)
        etQuantity = view.findViewById(R.id.et_quantity)
        btnAddItem = view.findViewById(R.id.btn_add_item)
        btnClear = view.findViewById(R.id.btn_clear)
        btnComplete = view.findViewById(R.id.btn_complete)
        tvTotalPrice = findTotalPriceTextView(view)
        itemsContainer = findShoppingItemsContainer(view)
    }

    private fun setupListeners() {
        btnAddItem.setOnClickListener { addNewItem() }
        btnClear.setOnClickListener { clearCheckedItems() }
        btnComplete.setOnClickListener { completeShoppingSession() }
    }

    private fun addNewItem() {
        val name = etNewItem.text.toString().trim()
        val quantityStr = etQuantity.text.toString().trim()

        if (name.isNotEmpty() && quantityStr.isNotEmpty()) {
            val quantity = quantityStr.toIntOrNull() ?: 1
            val pricePerUnit = PriceHelper.getPrice(name)
            val newItem = ShoppingItem(name, quantity, pricePerUnit, false)

            ShoppingManager.addItem(newItem)
            ShoppingManager.saveItemsToPrefs(requireContext())
            refreshShoppingList()

            etNewItem.text.clear()
            etQuantity.text.clear()

            Toast.makeText(context, "Added: $name ($quantity)", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Please enter both name and quantity", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearCheckedItems() {
        val checkedCount = ShoppingManager.getCheckedItems().size
        if (checkedCount == 0) {
            Toast.makeText(context, "No items selected to remove", Toast.LENGTH_SHORT).show()
            return
        }

        ShoppingManager.removeCheckedItems()
        ShoppingManager.saveItemsToPrefs(requireContext())
        refreshShoppingList()

        Toast.makeText(context, "$checkedCount item(s) removed", Toast.LENGTH_SHORT).show()
    }

    private fun completeShoppingSession() {
        val checkedItemsCount = ShoppingManager.getCheckedItems().sumOf { it.quantity }
        if (checkedItemsCount > 0) {
            Toast.makeText(context, "Successfully purchased $checkedItemsCount item(s).", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "No items have been checked. Please select items you've purchased.", Toast.LENGTH_LONG).show()
        }
    }

    private fun refreshShoppingList() {
        val shoppingItems = ShoppingManager.getItems()

        var titleTextView: TextView? = null
        for (i in 0 until itemsContainer.childCount) {
            val child = itemsContainer.getChildAt(i)
            if (child is TextView && child.text.toString().contains("My Shopping List")) {
                titleTextView = child
                break
            }
        }

        itemsContainer.removeAllViews()
        titleTextView?.let { itemsContainer.addView(it) }

        shoppingItems.forEachIndexed { index, item ->
            val itemRow = createItemRow(item, index)
            itemsContainer.addView(itemRow)
        }

        updateItemCounts()
        updateTotalPrice()
    }

    private fun updateItemCounts() {
        val totalQuantity = ShoppingManager.getItems().sumOf { it.quantity }
        val checkedQuantity = ShoppingManager.getCheckedItems().sumOf { it.quantity }

        tvSelectedItems.text = resources.getString(R.string.items_count_format, totalQuantity)
        tvCheckedItems.text = checkedQuantity.toString()
        tvTotalItems.text = totalQuantity.toString()
    }

    private fun updateTotalPrice() {
        val totalPrice = ShoppingManager.calculateTotalCheckedPrice()
        tvTotalPrice.text = currencyFormatter.format(totalPrice)
    }

    private fun createItemRow(item: ShoppingItem, index: Int): LinearLayout {
        val rowLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dpToPx(8)
            }
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
            setBackgroundColor(Color.parseColor("#EEEEEE"))
        }

        val checkbox = CheckBox(context).apply {
            isChecked = item.checked
            setOnCheckedChangeListener { _, isChecked ->
                ShoppingManager.updateItem(index, isChecked)
                updateItemCounts()
                updateTotalPrice()
            }
        }

        val imageView = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(dpToPx(48), dpToPx(48)).apply {
                leftMargin = dpToPx(8)
                rightMargin = dpToPx(8)
            }
            setImageResource(ImageHelper.getImageResId(item.name))
            contentDescription = "Image of ${item.name}"
        }

        val nameAndQtyTextView = TextView(context).apply {
            text = "${item.name} (Qty: ${item.quantity})"
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
        }

        val priceTextView = TextView(context).apply {
            text = currencyFormatter.format(item.price * item.quantity)
            textSize = 16f
            setTypeface(null, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                leftMargin = dpToPx(8)
            }
        }

        val detailsLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            addView(nameAndQtyTextView)
        }

        rowLayout.addView(checkbox)
        rowLayout.addView(imageView)
        rowLayout.addView(detailsLayout)
        rowLayout.addView(priceTextView)

        return rowLayout
    }


    private fun findShoppingItemsContainer(view: View): ViewGroup {
        val cards = mutableListOf<CardView>()
        findAllCardViews(view, cards)

        for (card in cards) {
            val linearLayouts = mutableListOf<LinearLayout>()
            findDirectLinearLayouts(card, linearLayouts)

            for (layout in linearLayouts) {
                for (i in 0 until layout.childCount) {
                    val child = layout.getChildAt(i)
                    if (child is TextView && child.text.toString().contains("My Shopping List")) {
                        return layout
                    }
                }
            }
        }

        return LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
    }

    private fun findTotalPriceTextView(view: View): TextView {
        val linearLayouts = mutableListOf<ViewGroup>()
        findAllLinearLayouts(view, linearLayouts)

        for (layout in linearLayouts) {
            for (i in 0 until layout.childCount) {
                val child = layout.getChildAt(i)
                if (child is TextView && child.text.toString().contains("Shopping Summary")) {
                    val summaryCard = layout.parent as? ViewGroup
                    summaryCard?.let {
                        for (j in 0 until it.childCount) {
                            val summaryChild = it.getChildAt(j)
                            if (summaryChild is LinearLayout) {
                                for (k in 0 until summaryChild.childCount) {
                                    val innerChild = summaryChild.getChildAt(k)
                                    if (innerChild is LinearLayout) {
                                        for (l in 0 until innerChild.childCount) {
                                            val textView = innerChild.getChildAt(l)
                                            if (textView is TextView && textView.text.toString().contains("$")
                                            ) return textView
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return TextView(context).apply { text = "$0.00" }
    }

    private fun findAllCardViews(view: View, result: MutableList<CardView>) {
        if (view is ViewGroup) {
            if (view is CardView) result.add(view)
            for (i in 0 until view.childCount) {
                findAllCardViews(view.getChildAt(i), result)
            }
        }
    }

    private fun findDirectLinearLayouts(parent: ViewGroup, result: MutableList<LinearLayout>) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is LinearLayout) result.add(child)
        }
    }

    private fun findAllLinearLayouts(view: View, result: MutableList<ViewGroup>) {
        if (view is ViewGroup) {
            if (view is LinearLayout) result.add(view)
            for (i in 0 until view.childCount) {
                findAllLinearLayouts(view.getChildAt(i), result)
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale).toInt()
    }

    override fun onResume() {
        super.onResume()
        refreshShoppingList()
    }
}