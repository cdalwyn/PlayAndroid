package me.goldze.mvvmhabit.binding.viewadapter.searchview;

import androidx.databinding.BindingAdapter;

import com.arlib.floatingsearchview.FloatingSearchView;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author Alwyn
 * @Date 2020/11/4
 * @Description
 */
public class ViewAdapter {
    @BindingAdapter("onSearchLeftCommand")
    public static void onSearchLeftActionClick(FloatingSearchView searchView, BindingCommand bindingCommand) {
        searchView.setOnHomeActionClickListener(() -> {
            if (bindingCommand != null) {
                bindingCommand.execute();
            }
        });
    }
}
