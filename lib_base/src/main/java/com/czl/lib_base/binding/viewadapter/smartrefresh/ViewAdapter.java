package com.czl.lib_base.binding.viewadapter.smartrefresh;

import androidx.databinding.BindingAdapter;

import com.czl.lib_base.binding.command.BindingCommand;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;


/**
 * Created by goldze on 2017/6/18.
 */
public class ViewAdapter {
    //下拉刷新命令
    @BindingAdapter({"onRefreshCommand"})
    public static void onRefreshCommand(SmartRefreshLayout smartRefreshLayout, final BindingCommand onRefreshCommand) {
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            if (onRefreshCommand != null) {
                onRefreshCommand.execute();
            }
        });
    }

    @BindingAdapter({"onLoadMoreCommand"})
    public static void onLoadMoreCommand(SmartRefreshLayout smartRefreshLayout, final BindingCommand onLoadCommand) {
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (onLoadCommand != null) {
                onLoadCommand.execute();
            }
        });
    }


}
