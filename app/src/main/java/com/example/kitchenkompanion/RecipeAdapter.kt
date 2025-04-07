package com.example.kitchenkompanion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ShapeAdapter(
    context: Context,
    resource: Int,
    shapeList: List<Item>
) : ArrayAdapter<Item>(context, resource, shapeList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)!!
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.recipe_cell, parent, false)

        val name = view.findViewById<TextView>(R.id.recipeName)
        val image = view.findViewById<ImageView>(R.id.recipeImage)

        name.text = item.name
        image.setImageResource(item.image)

        return view
    }
}