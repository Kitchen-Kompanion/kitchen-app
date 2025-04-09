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
    interface ShoppingListListener {
        fun addIngredientsToShoppingList(ingredients: List<String>, recipeName: String)
    }

    private var shoppingListListener: ShoppingListListener? = null

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
            listOf("Vegetarian", "Breakfast", "Halal", "Kosher"),
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
        ),
        RecipeItem(
            "Vegetable Stir Fry",
            "Sauté: In a wok or large skillet add 1 Tablespoon olive oil over medium high heat. Add bell pepper, peas, carrots, mushrooms, broccoli, baby corn, and water chestnuts. Sauté 2-3 minutes until veggies are almost tender.\nWhisk: In a small bowl, whisk together soy sauce, garlic, brown sugar, sesame oil, chicken broth, and cornstarch.\nMix: Pour over veggies and cook until the sauce has thickened.\nGarnish: add chopped green onions and sesame seeds if desired.",
            listOf("2 cups mixed vegetables", "1 tbsp olive oil", "2 cloves garlic", "1 tsp ginger", "2 tbsp soy sauce", "1 cup cooked rice"),
            listOf("Vegetarian", "Halal", "Kosher"),
            R.drawable.vegetable_stir_fry
        ),
        RecipeItem(
            name = "Chicken Curry",
            description = "Heat olive oil in a skillet over medium heat. Sauté onion until lightly browned.\nStir in garlic, curry powder, cinnamon, paprika, bay leaf, ginger, sugar, and salt. Continue stirring for 2 minutes.\nAdd chicken pieces, tomato paste, yogurt, and coconut milk. Bring to a boil, reduce heat, and simmer for 20-25 minutes.\nRemove bay leaf, and stir in lemon juice and cayenne pepper. Simmer 5 more minutes.\nServe hot and enjoy!",
            ingredients = listOf(
                "3 tbsp olive oil",
                "1 small onion, chopped",
                "2 cloves garlic, minced",
                "3 tbsp curry powder",
                "1 tsp ground cinnamon",
                "1 tsp paprika",
                "1 bay leaf",
                "½ tsp grated fresh ginger",
                "½ tsp white sugar",
                "Salt to taste",
                "2 skinless, boneless chicken breasts (cut bite-size)",
                "1 tbsp tomato paste",
                "1 cup plain yogurt",
                "¾ cup coconut milk",
                "½ lemon, juiced",
                "½ tsp cayenne pepper"
            ),
            tags = listOf("Chicken", "Halal", "Dinner"),
            img = R.drawable.chicken_curry
        ),
        RecipeItem(
            name = "Instant Pot Beef Stew",
            description = "Prep:\n1. Cut beef into small chunks, along with potatoes and carrots\n2. Season beef chunks with salt and pepper, then sprinkle with flour and toss to coat\n\nCooking:\n1. Set Instant Pot to sauté mode. Add oil and brown beef for 3-4 minutes. Remove and set aside\n2. Add onions and garlic to pot. Cook for 2-3 minutes until brown and soft\n3. Pour in broth to deglaze, scraping up brown bits from bottom. Stir for 1 minute\n4. Add tomato paste, seasoning, and Worcestershire sauce. Stir for 2 minutes\n5. Return beef to pot, top with potatoes and carrots. Add bay leaf\n\nPressure Cooking:\n1. Close lid, set steam release valve to seal\n2. Cook on high pressure for 35 minutes\n3. Allow 10 minute natural release before opening\n4. Remove bay leaf before serving",
            ingredients = listOf(
                "2 lbs beef chuck (cut into chunks)",
                "1 tbsp salt",
                "1 tsp black pepper",
                "¼ cup all-purpose flour",
                "2 tbsp olive oil",
                "1 large onion (chopped)",
                "4 cloves garlic (minced)",
                "4 cups beef broth",
                "2 tbsp tomato paste",
                "1 tbsp Worcestershire sauce",
                "1 tsp thyme",
                "1 tsp rosemary",
                "4 medium potatoes (cubed)",
                "3 carrots (chopped)",
                "1 bay leaf"
            ),
            tags = listOf("Beef", "Dinner", "Comfort Food"),
            img = R.drawable.beef_stew
        )
    )

    private val checked = mutableMapOf<String, Boolean>().withDefault { false }
    private val filteredRecipes = mutableListOf<RecipeItem>()

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is ShoppingListListener) {
            shoppingListListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        listOf(
            view.findViewById<LinearLayout>(R.id.ll1),
            view.findViewById<LinearLayout>(R.id.ll2),
            view.findViewById<LinearLayout>(R.id.ll3),
            view.findViewById<LinearLayout>(R.id.ll4),
            view.findViewById<LinearLayout>(R.id.ll5),
            view.findViewById<LinearLayout>(R.id.ll6),
            view.findViewById<LinearLayout>(R.id.ll7),
            view.findViewById<LinearLayout>(R.id.ll8),

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
                    active.all { filter ->
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
            view?.findViewById<LinearLayout>(R.id.ll5),
            view?.findViewById<LinearLayout>(R.id.ll6),
            view?.findViewById<LinearLayout>(R.id.ll7),
            view?.findViewById<LinearLayout>(R.id.ll8),
            )

        recipeLayouts.forEach { it?.visibility = View.GONE }

        view?.findViewById<TextView>(R.id.tvNoRecipes)?.visibility =
            if (filteredRecipes.isEmpty()) View.VISIBLE else View.GONE

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
                    it.findViewById<ImageView>(R.id.img1)?.setImageResource(recipeItem.img)
                }

                R.id.ll2 -> {
                    it.findViewById<TextView>(R.id.tvName2)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription2)?.text = recipeItem.description
                    it.findViewById<ImageView>(R.id.img2)?.setImageResource(recipeItem.img)
                }

                R.id.ll3 -> {
                    it.findViewById<TextView>(R.id.tvName3)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription3)?.text = recipeItem.description
                    it.findViewById<ImageView>(R.id.img3)?.setImageResource(recipeItem.img)
                }

                R.id.ll4 -> {
                    it.findViewById<TextView>(R.id.tvName4)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription4)?.text = recipeItem.description
                    it.findViewById<ImageView>(R.id.img4)?.setImageResource(recipeItem.img)
                }

                R.id.ll5 -> {
                    it.findViewById<TextView>(R.id.tvName5)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription5)?.text = recipeItem.description
                    it.findViewById<ImageView>(R.id.img5)?.setImageResource(recipeItem.img)
                }

                R.id.ll6 -> {
                    it.findViewById<TextView>(R.id.tvName6)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription6)?.text = recipeItem.description
                    it.findViewById<ImageView>(R.id.img6)?.setImageResource(recipeItem.img)
                }

                R.id.ll7 -> {
                    it.findViewById<TextView>(R.id.tvName7)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription7)?.text = recipeItem.description
                    it.findViewById<ImageView>(R.id.img7)?.setImageResource(recipeItem.img)
                }

                R.id.ll8 -> {
                    it.findViewById<TextView>(R.id.tvName8)?.text = recipeItem.name
                    it.findViewById<TextView>(R.id.tvDescription8)?.text = recipeItem.description
                    it.findViewById<ImageView>(R.id.img8)?.setImageResource(recipeItem.img)
                }

                else -> {return}
            }
        }
    }

    private fun showRecipeDialog(recipe: RecipeItem) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.recipes_popup, null)

        val imageView = dialogView.findViewById<ImageView>(R.id.ivRecipeImage)
        val ingredientsContainer = dialogView.findViewById<LinearLayout>(R.id.llIngredients)
        val nameTextView = dialogView.findViewById<TextView>(R.id.tvRecipeName)
        val tagsTextView = dialogView.findViewById<TextView>(R.id.tvRecipeTags)
        val descriptionTextView = dialogView.findViewById<TextView>(R.id.tvRecipeDescription)
        val closeButton = dialogView.findViewById<ImageView>(R.id.ivClose)

        imageView?.setImageResource(recipe.img)
        nameTextView?.text = recipe.name
        tagsTextView?.text = recipe.tags.joinToString(", ")
        descriptionTextView?.text = recipe.description

        ingredientsContainer?.removeAllViews()
        recipe.ingredients.forEach { ingredient ->
            TextView(requireContext()).apply {
                text = "• $ingredient"
                textSize = 16f
                setPadding(0, 4, 0, 4)
                ingredientsContainer?.addView(this)
            }
        }

        dialogView.findViewById<Button>(R.id.btnAddToShoppingList)?.setOnClickListener {
            shoppingListListener?.addIngredientsToShoppingList(recipe.ingredients, recipe.name)

            Toast.makeText(context, "Added to shopping list", Toast.LENGTH_SHORT).show()
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setBackground(resources.getDrawable(R.drawable.recipe_background, null))
            .show()

        dialog.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawableResource(R.drawable.recipe_background)
        }

        closeButton?.setOnClickListener {
            dialog.dismiss()
        }
    }
}