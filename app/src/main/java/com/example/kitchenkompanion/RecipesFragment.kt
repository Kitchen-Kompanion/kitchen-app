package com.example.kitchenkompanion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RecipesFragment : Fragment() {
    // Define interface for shopping list interaction
    interface ShoppingListListener {
        fun addIngredientsToShoppingList(ingredients: List<String>, recipeName: String)
    }

    // Listener property - may be null if parent doesn't implement interface
    private var shoppingListListener: ShoppingListListener? = null

    data class RecipeItem(
        val name: String,
        val description: String,
        val ingredients: List<String>,
        val tags: List<String>
    )

    private val allRecipeItems = listOf(
        RecipeItem(
            "Avocado Toast",
            "Mash a ripe avocado with a squeeze of lemon juice and a pinch of salt. \nSpread it evenly on a toasted slice of bread. \nSprinkle with red pepper flakes for a bit of heat.",
            listOf(
                "1 ripe avocado",
                "1 slice of bread",
                "1/2 lemon",
                "Salt to taste",
                "Red pepper flakes (optional)"
            ),
            listOf("Vegetarian", "Breakfast", "Snack", "Halal", "Kosher")
        ),
        RecipeItem(
            "Scrambled Eggs",
            "Whisk two eggs with a splash of milk, salt, and pepper. \nMelt butter in a pan, pour in the eggs, and gently scramble until cooked through but still moist. \nServe immediately.",
            listOf(
                "2 eggs",
                "1 tbsp milk",
                "1 tsp butter",
                "Salt and pepper to taste"
            ),
            listOf("Vegetarian","Breakfast","Halal", "Kosher")
        ),
        RecipeItem(
            "Tuna Salad",
            "Combine a can of drained tuna with mayonnaise, diced celery, and a squeeze of lemon juice. \nMix well and season with salt and pepper. \nServe on lettuce or bread.",
            listOf(
                "1 can tuna",
                "2 tbsp mayonnaise",
                "1 celery stalk",
                "1/2 lemon",
                "Salt and pepper to taste",
                "Lettuce or bread for serving"
            ),
            listOf("Fish", "Kosher")
        ),
        RecipeItem(
            "Caprese Salad",
            "Slice fresh mozzarella and ripe tomatoes.\nArrange them on a plate, alternating slices. \nDrizzle with olive oil, balsamic glaze, and sprinkle with fresh basil leaves.",
            listOf(
                "200g fresh mozzarella",
                "2 ripe tomatoes",
                "Fresh basil leaves",
                "2 tbsp olive oil",
                "1 tbsp balsamic glaze",
                "Salt and pepper to taste"
            ),
            listOf("Vegetarian", "Halal", "Kosher")
        ),
        RecipeItem(
            "Microwave Oatmeal",
            "Combine rolled oats, water or milk, and a pinch of salt in a microwave-safe bowl. \nMicrowave for 2-3 minutes, stirring occasionally. \nTop with fruit and nuts.",
            listOf(
                "1/2 cup rolled oats",
                "1 cup water or milk",
                "Pinch of salt",
                "Fruit and nuts for topping"
            ),
            listOf("Vegetarian", "Breakfast")
        )
    )

    private val checked = mutableMapOf<String, Boolean>().withDefault { false }
    private val filteredRecipes = mutableListOf<RecipeItem>()

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        // Try to get shopping list listener from parent activity
        if (context is ShoppingListListener) {
            shoppingListListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filteredRecipes.addAll(allRecipeItems)

        setupFilterCheckboxes(view)
        setupRecipeClickListeners(view)
        updateVisibleRecipes()
    }

    private fun setupRecipeClickListeners(view: View) {
        // Set up recipe listeners
        listOf(
            view.findViewById<LinearLayout>(R.id.ll1),
            view.findViewById<LinearLayout>(R.id.ll2),
            view.findViewById<LinearLayout>(R.id.ll3),
            view.findViewById<LinearLayout>(R.id.ll4),
            view.findViewById<LinearLayout>(R.id.ll5)
        ).forEachIndexed { index, layout ->
            layout?.setOnClickListener {
                if (index < filteredRecipes.size) {
                    showRecipeDialog(filteredRecipes[index])
                }
            }
        }
    }

    private fun setupFilterCheckboxes(view: View) {
        listOf(
            view.findViewById<CheckBox>(R.id.cbVegetarian),
            view.findViewById<CheckBox>(R.id.cbBeef),
            view.findViewById<CheckBox>(R.id.cbChicken),
            view.findViewById<CheckBox>(R.id.cbPork),
            view.findViewById<CheckBox>(R.id.cbFish),
            view.findViewById<CheckBox>(R.id.cbHalal),
            view.findViewById<CheckBox>(R.id.cbKosher),
            view.findViewById<CheckBox>(R.id.cbBreakfast),
            view.findViewById<CheckBox>(R.id.cbSnack)
        ).forEach { checkbox ->
            checkbox?.setOnCheckedChangeListener { _, isChecked ->
                checkbox.text?.toString()?.let {
                    checked[it] = isChecked
                }
                filterRecipes()
                updateVisibleRecipes()
            }
        }
    }

    private fun filterRecipes() {
        filteredRecipes.clear()

        val active = checked.filter { it.value }.keys

        if (active.isEmpty()) {
            filteredRecipes.addAll(allRecipeItems)
        } else {
            filteredRecipes.addAll(
                allRecipeItems.filter { recipe ->
                    active.any { filter ->
                        recipe.tags.any { tag ->
                            tag.trim().equals(
                                filter.trim(),
                                ignoreCase = true
                            )
                        }
                    }
                }
            )
        }
    }

    private fun updateVisibleRecipes() {
        val recipeLayouts = listOf(
            view?.findViewById<LinearLayout>(R.id.ll1),
            view?.findViewById<LinearLayout>(R.id.ll2),
            view?.findViewById<LinearLayout>(R.id.ll3),
            view?.findViewById<LinearLayout>(R.id.ll4),
            view?.findViewById<LinearLayout>(R.id.ll5)
        )

        // Hide all recipe layouts first
        recipeLayouts.forEach { it?.visibility = View.GONE }

        // Show "no recipes" message if filtered list is empty
        view?.findViewById<TextView>(R.id.tvNoRecipes)?.visibility =
            if (filteredRecipes.isEmpty()) View.VISIBLE else View.GONE

        // Show only filtered recipes
        filteredRecipes.take(recipeLayouts.size).forEachIndexed { index, recipeItem ->
            if (index < recipeLayouts.size) {
                recipeLayouts[index]?.visibility = View.VISIBLE
                updateRecipeView(recipeLayouts[index], recipeItem)
            }
        }
    }

    private fun updateRecipeView(linearLayout: LinearLayout?, recipeItem: RecipeItem) {
        linearLayout?.let {
            when (it.id) {
                R.id.ll1 -> {
                    it.findViewById<TextView>(R.id.tvName1)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription1)?.text = recipeItem.description
                }
                R.id.ll2 -> {
                    it.findViewById<TextView>(R.id.tvName2)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription2)?.text = recipeItem.description
                }
                R.id.ll3 -> {
                    it.findViewById<TextView>(R.id.tvName3)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription3)?.text = recipeItem.description
                }
                R.id.ll4 -> {
                    it.findViewById<TextView>(R.id.tvName4)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription4)?.text = recipeItem.description
                }
                R.id.ll5 -> {
                    it.findViewById<TextView>(R.id.tvName5)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription5)?.text = recipeItem.description
                }
            }
        }
    }

    private fun showRecipeDialog(recipe: RecipeItem) {
        // Inflate the recipes_popup.xml layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.recipes_popup, null)

        // Find views in the dialog layout
        val ingredientsContainer = dialogView.findViewById<LinearLayout>(R.id.llIngredients)
        val nameTextView = dialogView.findViewById<TextView>(R.id.tvRecipeName)
        val tagsTextView = dialogView.findViewById<TextView>(R.id.tvRecipeTags)
        val descriptionTextView = dialogView.findViewById<TextView>(R.id.tvRecipeDescription)
        val closeButton = dialogView.findViewById<ImageView>(R.id.ivClose)

        // Set recipe details
        nameTextView?.text = recipe.name
        tagsTextView?.text = recipe.tags.joinToString(", ")
        descriptionTextView?.text = recipe.description

        // Add ingredients to the container
        ingredientsContainer?.removeAllViews()
        recipe.ingredients.forEach { ingredient ->
            TextView(requireContext()).apply {
                text = "• $ingredient"
                textSize = 16f
                setPadding(0, 4, 0, 4)
                ingredientsContainer?.addView(this)
            }
        }

        // Try to find and set up the "Add to Shopping List" button if it exists
        dialogView.findViewById<Button>(R.id.btnAddToShoppingList)?.setOnClickListener {
            // Use shopping list listener if available
            shoppingListListener?.addIngredientsToShoppingList(recipe.ingredients, recipe.name)

            // Show confirmation message
            Toast.makeText(context, "Added to shopping list", Toast.LENGTH_SHORT).show()
        }

        // Create and show the dialog
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setBackground(resources.getDrawable(R.drawable.recipe_background, null))
            .show()

        // Configure dialog window
        dialog.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawableResource(R.drawable.recipe_background)
        }

        // Set up close button
        closeButton?.setOnClickListener {
            dialog.dismiss()
        }
    }
}