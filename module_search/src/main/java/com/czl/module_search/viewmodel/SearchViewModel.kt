package com.czl.module_search.viewmodel

import android.view.View
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

import me.goldze.mvvmhabit.utils.ToastUtils

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class SearchViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val onSearchLeftCommand:BindingCommand<Void> = BindingCommand(BindingAction {
        finish()
    })
}