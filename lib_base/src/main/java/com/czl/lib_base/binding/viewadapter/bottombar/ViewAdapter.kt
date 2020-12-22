package com.czl.lib_base.binding.viewadapter.bottombar

import androidx.databinding.BindingAdapter
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.czl.lib_base.binding.command.BindingCommand

/**
 * @author Alwyn
 * @Date 2020/10/29
 * @Description
 */
object ViewAdapter {
    @JvmStatic
    @BindingAdapter(value = ["onTabChangeCommand"], requireAll = false)
    fun onTabChangeCommand(
        bar: BottomNavigationBar,
        onTabSelectedCommand: BindingCommand<Int?>?
    ) {
        bar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                onTabSelectedCommand?.execute(position)
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {}
        })
    }
}