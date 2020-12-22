package com.czl.lib_base.binding.viewadapter.smartrefresh

import androidx.databinding.BindingAdapter
import com.czl.lib_base.binding.command.BindingCommand
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout

object ViewAdapter {
    //下拉刷新命令
    @JvmStatic
    @BindingAdapter("onRefreshCommand")
    fun onRefreshCommand(
        smartRefreshLayout: SmartRefreshLayout,
        onRefreshCommand: BindingCommand<*>?
    ) {
        smartRefreshLayout.setOnRefreshListener { refreshLayout: RefreshLayout? -> onRefreshCommand?.execute() }
    }

    @JvmStatic
    @BindingAdapter("onLoadMoreCommand")
    fun onLoadMoreCommand(
        smartRefreshLayout: SmartRefreshLayout,
        onLoadCommand: BindingCommand<*>?
    ) {
        smartRefreshLayout.setOnLoadMoreListener { refreshLayout: RefreshLayout? -> onLoadCommand?.execute() }
    }
}