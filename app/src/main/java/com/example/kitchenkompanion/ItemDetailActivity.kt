package com.example.kitchenkompanion

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var item: InventoryItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        item = intent.getSerializableExtra("item") as? InventoryItem ?: return

        val typeSpinner = findViewById<Spinner>(R.id.edit_type)
        val types = arrayOf("Fruits", "Dairy", "Pantry", "Frozen", "Beverages", "Snacks")
        typeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
        val currentIndex = types.indexOf(item.type)
        if (currentIndex >= 0) {
            typeSpinner.setSelection(currentIndex)
        }

        val nameEdit = findViewById<EditText>(R.id.edit_name)
        val ownerEdit = findViewById<EditText>(R.id.edit_owner)
        val countEdit = findViewById<EditText>(R.id.edit_count)
        val expireEdit = findViewById<EditText>(R.id.edit_expire)
        val imageView = findViewById<ImageView>(R.id.detail_image)

        // Set the initial values
        nameEdit.setText(item.name)

        ownerEdit.setText(item.owner)
        countEdit.setText(item.count.toString())
        expireEdit.setText(item.expireDate)
        val imageResId = ImageHelper.getImageResId(item.name.lowercase())
        imageView.setImageResource(imageResId)

        // Back Button
        findViewById<Button>(R.id.back_button).setOnClickListener {
            finish()
        }
        // save button
        val saveButton = findViewById<Button>(R.id.btn_save)
        saveButton.setOnClickListener {
            val updatedItem = InventoryItem(
                name = nameEdit.text.toString(),
                type =typeSpinner.selectedItem.toString(),
                owner = ownerEdit.text.toString(),
                count = countEdit.text.toString().toIntOrNull() ?: 1,
                expireDate = expireEdit.text.toString()
            )

            InventoryManager.removeItem(item)     // item delete
            InventoryManager.addItem(updatedItem) // item add
            InventoryManager.saveItemsToPrefs(this)

            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
        val deleteButton = findViewById<Button>(R.id.btn_delete)
        deleteButton.setOnClickListener {
            InventoryManager.removeItem(item)
            InventoryManager.saveItemsToPrefs(this)

            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
            finish()
        }


    }
}
