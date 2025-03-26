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
            0 -> TextFragment()
            1 -> LRFragment()
            2 -> ColorsFragment()
            3 -> ProfileFragment()

            else -> TextFragment()
        }
    }

}