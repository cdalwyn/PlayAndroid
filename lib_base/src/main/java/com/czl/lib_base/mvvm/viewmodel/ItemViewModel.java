package com.czl.lib_base.mvvm.viewmodel;

import androidx.annotation.NonNull;

import com.czl.lib_base.base.BaseViewModel;

public class ItemViewModel<VM extends BaseViewModel> {
    protected VM viewModel;

    public ItemViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }
}