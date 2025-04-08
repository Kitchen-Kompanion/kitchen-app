package com.example.kitchenkompanion

import android.icu.text.Transliterator.Position
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

//todo: add images, integrate with shopping list

class RecipesFragment : Fragment() {
    data class RecipeItem(
        val name: String,
        val description: String,
        val ingredients: List<String>,
        val tags: List<String>,
        val img: Int
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
            listOf("Vegetarian", "Breakfast", "Snack", "Halal", "Kosher"),
            R.drawable.avocado_toast
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
            listOf("Vegetarian","Breakfast","Halal", "Kosher"),
            R.drawable.scrampled_eggs
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
            listOf("Fish", "Kosher"),
            R.drawable.tuna_salad
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
            listOf("Vegetarian", "Halal", "Kosher"),
            R.drawable.caprese_salad
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
            listOf("Vegetarian", "Breakfast"),
            R.drawable.oatmeal
        )
    )
    private val checked = mutableMapOf<String, Boolean>().withDefault { false }
    private val filteredRecipes = mutableListOf<RecipeItem>()


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
    }

    private fun setupRecipeClickListeners(view: View) {
        // Set up recipe listeners
        listOf<LinearLayout>(
            view.findViewById<LinearLayout>(R.id.ll1),
            view.findViewById<LinearLayout>(R.id.ll2),
            view.findViewById<LinearLayout>(R.id.ll3),
            view.findViewById<LinearLayout>(R.id.ll4),
            view.findViewById<LinearLayout>(R.id.ll5)
        ).forEachIndexed { index, layout ->
            layout.setOnClickListener {
                showRecipeDialog(filteredRecipes[index])
            }
        }
    }

    private fun setupFilterCheckboxes(view: View) {
        val checkBoxes = listOf(
            view.findViewById<CheckBox>(R.id.cbVegetarian),
            view.findViewById<CheckBox>(R.id.cbBeef),
            view.findViewById<CheckBox>(R.id.cbChicken),
            view.findViewById<CheckBox>(R.id.cbPork),
            view.findViewById<CheckBox>(R.id.cbFish),
            view.findViewById<CheckBox>(R.id.cbHalal),
            view.findViewById<CheckBox>(R.id.cbKosher),
            view.findViewById<CheckBox>(R.id.cbBreakfast),
            view.findViewById<CheckBox>(R.id.cbSnack)
        )


        checkBoxes.forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                checked[checkbox.text.toString()] = isChecked
                filterRecipes()
                updateVisibleRecipes()
                setupRecipeClickListeners(view)
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

        recipeLayouts.forEach { it?.visibility = View.GONE }

        filteredRecipes.take(recipeLayouts.size).forEachIndexed { index, recipeItem ->
            if (index < recipeLayouts.size) {
                recipeLayouts[index]?.visibility = View.VISIBLE
                updateRecipeView(recipeLayouts[index], recipeItem, index)
            }
        }
    }

    private fun updateRecipeView(linearLayout: LinearLayout?, recipeItem: RecipeItem, position: Int) {
        linearLayout?.let {
            val name = when(position){
                0 -> R.id.tvName1
                1 -> R.id.tvName2
                2 -> R.id.tvName3
                3 -> R.id.tvName4
                4 -> R.id.tvName5
                else -> return
            }
            val desc = when(position){
                0 -> R.id.tvDescription1
                1 -> R.id.tvDescription2
                2 -> R.id.tvDescription3
                3 -> R.id.tvDescription4
                4 -> R.id.tvDescription5
                else -> return
            }

            val img = when(position){
                0 -> R.id.img1
                1 -> R.id.img2
                2 -> R.id.img3
                3 -> R.id.img4
                4 -> R.id.img5
                else -> return
            }

            it.findViewById<TextView>(name)?.text = recipeItem.name
            it.findViewById<TextView>(desc)?.text = recipeItem.description
            it.findViewById<ImageView>(img)?.setImageResource(recipeItem.img)
        }
    }

    private fun showRecipeDialog(recipe: RecipeItem) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.recipes_popup, null)
        val ingredientsContainer = dialogView.findViewById<LinearLayout>(R.id.llIngredients)

        dialogView.findViewById<ImageView>(R.id.ivRecipeImage).setImageResource(recipe.img)
        dialogView.findViewById<TextView>(R.id.tvRecipeName).text = recipe.name
        dialogView.findViewById<TextView>(R.id.tvRecipeTags).text = recipe.tags.joinToString(", ")
        dialogView.findViewById<TextView>(R.id.tvRecipeDescription).text = recipe.description

        ingredientsContainer.removeAllViews()
        recipe.ingredients.forEach { ingredient ->
            TextView(requireContext()).apply {
                text = "• $ingredient"
                textSize = 16f
                setPadding(0, 4, 0, 4)
                ingredientsContainer.addView(this)
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setBackground(resources.getDrawable(R.drawable.recipe_background, null))
            .show()
            .also { dialog ->
                dialog.window?.let { window ->
                    window.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    window.setBackgroundDrawableResource(R.drawable.recipe_background)
                }
                dialogView.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
                    dialog.dismiss()
                }
            }
    }

}
