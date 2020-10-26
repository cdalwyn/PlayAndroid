package com.czl.module_main.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel
import com.czl.lib_base.data.entity.ArticleBean

import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.utils.ToastUtils

/**
 * @author Alwyn
 * @Date 2020/7/28
 * @Description
 */
class RvItemViewModel(mainViewModel: MainViewModel) : ItemViewModel<MainViewModel>(mainViewModel) {

    var entity: ObservableField<ArticleBean.Data> = ObservableField()

    constructor(mainViewModel: MainViewModel, data: ArticleBean.Data) : this(mainViewModel) {
        entity.set(data)
    }

    var itemOnClick: BindingCommand<Any?> = BindingCommand(BindingAction {
        viewModel.uc.deleteItemLiveData.value = this
        ToastUtils.showShort("${entity.get()?.title},下标=${viewModel.getItemPosition(this)}")
    })


}