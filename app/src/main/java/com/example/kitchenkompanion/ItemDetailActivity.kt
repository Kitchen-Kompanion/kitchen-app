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


        val nameEdit = findViewById<EditText>(R.id.edit_name)
        val typeEdit = findViewById<EditText>(R.id.edit_type)
        val ownerEdit = findViewById<EditText>(R.id.edit_owner)
        val countEdit = findViewById<EditText>(R.id.edit_count)
        val expireEdit = findViewById<EditText>(R.id.edit_expire)
        val imageView = findViewById<ImageView>(R.id.detail_image)

        // 기존 값 설정
        nameEdit.setText(item.name)
        typeEdit.setText(item.type)
        ownerEdit.setText(item.owner)
        countEdit.setText(item.count.toString())
        expireEdit.setText(item.expireDate)
        val imageResId = ImageHelper.getImageResId(item.name.lowercase())
        imageView.setImageResource(imageResId)

        // 저장 버튼
        val saveButton = findViewById<Button>(R.id.btn_save)
        saveButton.setOnClickListener {
            val updatedItem = InventoryItem(
                name = nameEdit.text.toString(),
                type = typeEdit.text.toString(),
                owner = ownerEdit.text.toString(),
                count = countEdit.text.toString().toIntOrNull() ?: 1,
                expireDate = expireEdit.text.toString()
            )

            InventoryManager.removeItem(item)     // 기존 삭제
            InventoryManager.addItem(updatedItem) // 수정한 거 저장
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
