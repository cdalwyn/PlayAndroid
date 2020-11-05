package me.goldze.mvvmhabit.binding.viewadapter.searchview;

import android.text.TextUtils;
import android.view.View;

import androidx.databinding.BindingAdapter;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * @author Alwyn
 * @Date 2020/11/4
 * @Description
 */
public class ViewAdapter {

    @BindingAdapter(value = {"onNavigationCommand", "onSearchConfirmCommand","onSearchStateCommand"}, requireAll = false)
    public static void onSearchDrawerClickCommand(MaterialSearchBar searchBar,
                                                  BindingCommand<Void> command,
                                                  BindingCommand<String> bindingCommand,
                                                  BindingCommand<Boolean> booleanCommand) {
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (booleanCommand!=null){
                    booleanCommand.execute(enabled);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                if (bindingCommand != null && !TextUtils.isEmpty(text)) {
                    bindingCommand.execute(text.toString());
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (MaterialSearchBar.BUTTON_NAVIGATION == buttonCode && command != null) {
                    command.execute();
                }
            }
        });
    }

    @BindingAdapter("onSuggestionItemCommand")
    public static void onSuggestionClick(MaterialSearchBar searchBar, BindingCommand<Integer> command) {
        searchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (command != null) {
                    command.execute(position);
                }
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });
    }

    @BindingAdapter("onSearchTextChangeCommand")
    public static void onSearchTextChangeCommand(MaterialSearchBar searchBar, BindingCommand<String> command) {
        RxTextView.textChanges(searchBar.getSearchEditText())
                .skip(1)
                .debounce(1L, TimeUnit.SECONDS)
                .map(x -> x.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    if (command != null) {
                        command.execute(x);
                    }
                });
    }
}
