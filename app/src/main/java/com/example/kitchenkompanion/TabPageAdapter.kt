package com.example.kitchenkompanion

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int) : FragmentStateAdapter(activity)
{
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment
    {
        return when (position)
        {
            0 -> ShoppingFragment()
            1 -> InventoryFragment()
            2 -> RecipesFragment()
            else -> ShoppingFragment()
        }
    }

}