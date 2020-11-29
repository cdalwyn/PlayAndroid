package com.czl.lib_base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.czl.lib_base.base.BaseRxFragment
import me.yokeyword.fragmentation.SupportFragment

class ViewPagerFmAdapter(
    manager: FragmentManager,
    lifeCycle:Lifecycle,
    private val mFragments: List<SupportFragment>
) : FragmentStateAdapter(manager,lifeCycle) {
    override fun getItemCount(): Int {
        return mFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }

}