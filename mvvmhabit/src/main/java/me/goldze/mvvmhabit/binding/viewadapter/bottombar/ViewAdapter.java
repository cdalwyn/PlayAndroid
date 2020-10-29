package me.goldze.mvvmhabit.binding.viewadapter.bottombar;

import androidx.databinding.BindingAdapter;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author Alwyn
 * @Date 2020/10/29
 * @Description
 */
public class ViewAdapter {
    @BindingAdapter(value = {"onTabChangeCommand"}, requireAll = false)
    public static void onTabChangeCommand(final BottomNavigationBar bar,
                                          final BindingCommand<Integer> onTabSelectedCommand) {
        bar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (onTabSelectedCommand != null) {
                    onTabSelectedCommand.execute(position);
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

    }
}
