package com.example.kitchenkompanion

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CategoryViewActivity : AppCompatActivity() {

    private lateinit var grid: GridLayout
    private lateinit var categoryName: String
    private lateinit var categoryTitle: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_item_list)

        val backBtn = findViewById<Button>(R.id.back_button)
        backBtn.setOnClickListener {
            finish()
        }

        categoryName = intent.getStringExtra("category") ?: "Category"
        categoryTitle = findViewById<TextView>(R.id.category_title)
        grid = findViewById<GridLayout>(R.id.category_grid_container)

        setupGrid(categoryName, grid, categoryTitle)
    }
    override fun onResume() {
        super.onResume()
        setupGrid(categoryName, grid, categoryTitle)
    }
    private fun setupGrid(categoryName: String, grid: GridLayout, categoryTitle: TextView) {
        categoryTitle.text = categoryName
        grid.removeAllViews()

        val items = InventoryManager.getItems().filter { it.type == categoryName }

        for (item in items) {
            val itemView = layoutInflater.inflate(R.layout.inventory_item, grid, false)

            itemView.findViewById<TextView>(R.id.item_name).text = item.name
            itemView.findViewById<TextView>(R.id.item_count).text = item.count.toString()
            val imageView = itemView.findViewById<ImageView>(R.id.item_image)
            val imageResId = ImageHelper.getImageResId(item.name.lowercase())
            imageView.setImageResource(imageResId)

            itemView.setOnClickListener {
                val intent = android.content.Intent(this, ItemDetailActivity::class.java)
                intent.putExtra("item", item)
                startActivity(intent)
            }

            grid.addView(itemView)
        }

        if (items.isEmpty()) {
            Toast.makeText(this, "No items in $categoryName", Toast.LENGTH_SHORT).show()
        }
    }

}
