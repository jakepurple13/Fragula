package com.fragula2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

internal class SwipeBackAdapter(private val fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val currentList = mutableListOf<FragulaEntry>()

    override fun createFragment(position: Int): Fragment {
        val context = fragment.requireContext()
        val entry = currentList[position]
        var className = entry.className
        if (className[0] == '.') {
            className = context.packageName + className
        }
        return fragment.childFragmentManager.fragmentFactory
            .instantiate(context.classLoader, className).apply {
                arguments = entry.arguments
            }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun push(fragulaEntry: FragulaEntry) {
        currentList.add(fragulaEntry)
        notifyItemInserted(currentList.size - 1)
    }

    fun pop() {
        val index = currentList.size - 1
        currentList.removeAt(index)
        notifyItemRemoved(index)
    }
}