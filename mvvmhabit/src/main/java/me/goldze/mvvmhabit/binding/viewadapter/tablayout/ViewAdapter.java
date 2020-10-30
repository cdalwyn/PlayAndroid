package me.goldze.mvvmhabit.binding.viewadapter.tablayout;

import androidx.databinding.BindingAdapter;

import com.google.android.material.tabs.TabLayout;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

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
