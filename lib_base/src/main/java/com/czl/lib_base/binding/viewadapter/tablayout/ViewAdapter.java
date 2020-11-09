package com.czl.lib_base.binding.viewadapter.tablayout;

import androidx.databinding.BindingAdapter;

import com.czl.lib_base.binding.command.BindingCommand;
import com.google.android.material.tabs.TabLayout;

/**
 * @author Alwyn
 * @Date 2020/10/31
 * @Description
 */
public class ViewAdapter {
    @BindingAdapter("onTabSelectedCommand")
    public static void onTabSelectedCommand(TabLayout tabLayout, final BindingCommand<Integer> bindingCommand) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (bindingCommand != null) {
                    bindingCommand.execute(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
