package com.example.kitchenkompanion

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InventoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        InventoryManager.init(requireContext())

        val view = inflater.inflate(R.layout.inventory, container, false)

        val addButton = view.findViewById<FloatingActionButton>(R.id.add_item_button)
        addButton.setOnClickListener {
            showAddItemDialog(view)
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        InventoryManager.init(requireContext())
        refreshInventory(view)
    }
    private fun refreshInventory(view: View) {
        Log.d("InventoryFragment", "HI")
        val inventoryList = InventoryManager.getItems()
        val fruitSection = view.findViewById<LinearLayout>(R.id.fruit_section)
        val dairySection = view.findViewById<LinearLayout>(R.id.dairy_section)
        val pantrySection = view.findViewById<LinearLayout>(R.id.pantry_section)
        val frozenSection = view.findViewById<LinearLayout>(R.id.frozen_section)
        val beverageSection = view.findViewById<LinearLayout>(R.id.beverages_section)
        val snackSection = view.findViewById<LinearLayout>(R.id.snacks_section)

        // 기존 뷰 제거 (중복 방지)
        fruitSection.removeAllViews()
        dairySection.removeAllViews()
        pantrySection.removeAllViews()
        frozenSection.removeAllViews()
        beverageSection.removeAllViews()
        snackSection.removeAllViews()



        for (item in inventoryList) {
            Log.d("InventoryFragment", item.toString())

            val itemView = layoutInflater.inflate(R.layout.inventory_item, null)
            Log.d("InventoryFragment", "Item: ${item.name}, Type: ${item.type}, Count: ${item.count}")
            itemView.findViewById<TextView>(R.id.item_name).text = item.name.toString()
            itemView.findViewById<TextView>(R.id.item_count).text = item.count.toString()
            itemView.findViewById<ImageView>(R.id.item_image).setImageResource(R.drawable.mango)

            when (item.type) {
                "Fruits" -> fruitSection.addView(itemView)
                "Dairy" -> dairySection.addView(itemView)
                "Pantry" -> pantrySection.addView(itemView)
                "Frozen" -> frozenSection.addView(itemView)
                "Beverages" -> beverageSection.addView(itemView)
                "Snacks" -> snackSection.addView(itemView)
            }
        }
    }



    private fun showAddItemDialog(view : View) {
        val dialogView = layoutInflater.inflate(R.layout.add_item, null)

        val itemName = dialogView.findViewById<EditText>(R.id.itemName)
        val itemOwner = dialogView.findViewById<EditText>(R.id.itemOwner)
        val itemType = dialogView.findViewById<Spinner>(R.id.spinner_type)
        val itemCount = dialogView.findViewById<EditText>(R.id.edit_count)
        val expireDate = dialogView.findViewById<EditText>(R.id.edit_expire_date)

        // Spinner 옵션 추가
        val types = arrayOf("Dairy", "Fruits", "Pantry", "Frozen", "Beverages", "Snacks")
        itemType.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, types)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // 버튼 리스너 설정
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnAdd = dialogView.findViewById<Button>(R.id.btn_add)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnAdd.setOnClickListener {
            val newItem = InventoryItem(
                name = itemName.text.toString(),
                owner = itemOwner.text.toString(),
                type = itemType.selectedItem.toString(),
                count = itemCount.text.toString().toIntOrNull() ?: 1,
                expireDate = expireDate.text.toString()
            )
            InventoryManager.addItem(newItem)
            InventoryManager.saveItemsToPrefs(requireContext())

            Toast.makeText(requireContext(), "Item added!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            refreshInventory(requireView())

        }
        dialog.show()
    }
}
