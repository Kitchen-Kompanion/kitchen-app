package com.example.kitchenkompanion

import java.text.SimpleDateFormat
import java.util.*
import android.app.AlertDialog
import android.content.Intent
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

        val fruitsButton = view.findViewById<TextView>(R.id.btn_more_fruits)
        val pantryButton = view.findViewById<TextView>(R.id.btn_more_pantry)
        val frozenButton = view.findViewById<TextView>(R.id.btn_more_frozen)
        val dairyButton = view.findViewById<TextView>(R.id.btn_more_dairy)
        val beveragesButton = view.findViewById<TextView>(R.id.btn_more_beverages)
        val snacksButton = view.findViewById<TextView>(R.id.btn_more_snacks)

        fruitsButton.setOnClickListener {
            openCategory("Fruits")
        }
        pantryButton.setOnClickListener {
            openCategory("Pantry")
        }
        frozenButton.setOnClickListener {
            openCategory("Frozen")
        }
        dairyButton.setOnClickListener {
            openCategory("Dairy")
        }
        beveragesButton.setOnClickListener {
            openCategory("Beverages")
        }
        snacksButton.setOnClickListener {
            openCategory("Snacks")
        }



    }
    private fun openCategory(category: String) {
        val intent = Intent(requireContext(), CategoryViewActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        refreshInventory(requireView())
    }
    private fun refreshInventory(view: View) {
        Log.d("InventoryFragment", "HI")
        val inventoryList = InventoryManager.getItems()
        val expireSection = view.findViewById<LinearLayout>(R.id.expire_section)
        val fruitSection = view.findViewById<LinearLayout>(R.id.fruit_section)
        val dairySection = view.findViewById<LinearLayout>(R.id.dairy_section)
        val pantrySection = view.findViewById<LinearLayout>(R.id.pantry_section)
        val frozenSection = view.findViewById<LinearLayout>(R.id.frozen_section)
        val beverageSection = view.findViewById<LinearLayout>(R.id.beverages_section)
        val snackSection = view.findViewById<LinearLayout>(R.id.snacks_section)

        expireSection.removeAllViews()
        fruitSection.removeAllViews()
        dairySection.removeAllViews()
        pantrySection.removeAllViews()
        frozenSection.removeAllViews()
        beverageSection.removeAllViews()
        snackSection.removeAllViews()


        val uniqueItemCount = inventoryList.size
        view.findViewById<TextView>(R.id.tv_selected_items).text = "Items: $uniqueItemCount"

        val expiringItems = getExpiringSoonItems()
        for (item in expiringItems) {
            val itemView = layoutInflater.inflate(R.layout.inventory_item, null)
            itemView.findViewById<TextView>(R.id.item_name).text = item.name
            itemView.findViewById<TextView>(R.id.item_count).text = item.count.toString()
            val imageResId = ImageHelper.getImageResId(item.name.lowercase())
            itemView.findViewById<ImageView>(R.id.item_image).setImageResource(imageResId)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val marginInDp = (12 * resources.displayMetrics.density).toInt()
            params.setMargins(0, 0, marginInDp, 0)
            itemView.layoutParams = params

            itemView.setOnClickListener {
                val intent = Intent(requireContext(), ItemDetailActivity::class.java)
                intent.putExtra("item", item)
                startActivity(intent)
            }

            expireSection.addView(itemView)
        }
        for (item in inventoryList) {
            Log.d("InventoryFragment", item.toString())

            val itemView = layoutInflater.inflate(R.layout.inventory_item, null)
            Log.d("InventoryFragment", "Item: ${item.name}, Type: ${item.type}, Count: ${item.count}")
            itemView.findViewById<TextView>(R.id.item_name).text = item.name.toString()
            itemView.findViewById<TextView>(R.id.item_count).text = item.count.toString()
            val imageName = item.name.lowercase()

            val imageView = itemView.findViewById<ImageView>(R.id.item_image)
            val imageResId = ImageHelper.getImageResId(item.name.lowercase())
            imageView.setImageResource(imageResId)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val marginInDp = (12 * resources.displayMetrics.density).toInt()
            params.setMargins(0, 0, marginInDp, 0)
            itemView.layoutParams = params



            itemView.setOnClickListener {
                val intent = Intent(requireContext(), ItemDetailActivity::class.java)
                intent.putExtra("item", item)
                startActivity(intent)
            }

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


    private fun getExpiringSoonItems(): List<InventoryItem> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val today = Calendar.getInstance().time

        return InventoryManager.getItems().filter { item ->
            try {
                val expire = dateFormat.parse(item.expireDate)
                val diff = (expire.time - today.time) / (1000 * 60 * 60 * 24)
                diff in 0..7
            } catch (e: Exception) {
                false
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

        val types = arrayOf("Dairy", "Fruits", "Pantry", "Frozen", "Beverages", "Snacks")
        itemType.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, types)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

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
