package com.czl.lib_base.binding.viewadapter.tablayout

import androidx.databinding.BindingAdapter
import com.czl.lib_base.binding.command.BindingCommand
import com.google.android.material.tabs.TabLayout

/**
 * @author Alwyn
 * @Date 2020/10/31
 * @Description
 */
object ViewAdapter {
    @JvmStatic
    @BindingAdapter("onTabSelectedCommand")
    fun onTabSelectedCommand(tabLayout: TabLayout, bindingCommand: BindingCommand<Int>?) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                bindingCommand?.execute(tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}