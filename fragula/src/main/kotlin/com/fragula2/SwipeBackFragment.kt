package com.fragula2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavGraph
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2

internal class SwipeBackFragment : Fragment(R.layout.fragment_swipeback) {

    private val navController by lazy { findNavController() }
    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == ViewPager2.SCROLL_STATE_IDLE && shouldPop) {
                val itemCount = swipeBackAdapter?.itemCount ?: 0
                val currentItem = viewPager?.currentItem ?: 0
                if (itemCount - 1 > currentItem && !running) {
                    navController.popBackStack()
                }
            }
        }
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            shouldPop = position + positionOffset < scrollOffset
            scrollOffset = position + positionOffset
        }
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewPager?.requestTransform()
        }
    }

    private val nextItem: Int
        get() = viewPager?.currentItem?.plus(1) ?: 0
    private val prevItem: Int
        get() = viewPager?.currentItem?.minus(1) ?: 0

    private var viewPager: ViewPager2? = null
    private var swipeBackAdapter: SwipeBackAdapter? = null
    private var running = false
    private var shouldPop = false
    private var scrollOffset = 0.0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById<ViewPager2?>(R.id.viewPager).also { viewPager ->
            viewPager.registerOnPageChangeCallback(onPageChangeCallback)
            viewPager.setPageTransformer(SwipeBackTransformer())
            viewPager.pageOverScrollMode = View.OVER_SCROLL_NEVER
            viewPager.adapter = SwipeBackAdapter(this).also { adapter ->
                swipeBackAdapter = adapter
            }
        }
        for (entry in navController.backQueue) {
            if (entry.destination is NavGraph) continue
            val destination = entry.destination as FragmentNavigator.Destination
            val fragulaEntry = FragulaEntry(
                className = destination.className,
                arguments = entry.arguments
            )
            navigate(fragulaEntry)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.unregisterOnPageChangeCallback(onPageChangeCallback)
        swipeBackAdapter = null
        viewPager = null
    }

    fun navigate(fragulaEntry: FragulaEntry) {
        swipeBackAdapter?.push(fragulaEntry)
        viewPager?.isUserInputEnabled = false
        viewPager?.setCurrentItemInternal(nextItem) {
            viewPager?.isUserInputEnabled = true
        }
    }

    fun popBackStack() {
        if (!running) {
            running = true
            viewPager?.isUserInputEnabled = false
            viewPager?.setCurrentItemInternal(prevItem) {
                swipeBackAdapter?.pop()
                viewPager?.isUserInputEnabled = true
                running = false
            }
        }
    }
}