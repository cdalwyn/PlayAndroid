package com.czl.module_main.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel
import com.czl.lib_base.data.bean.ArticleBean
import com.czl.lib_base.util.ToastHelper


/**
 * @author Alwyn
 * @Date 2020/7/28
 * @Description
 */
class RvItemViewModel(testViewModel: TestViewModel) : ItemViewModel<TestViewModel>(testViewModel) {

    var entity: ObservableField<ArticleBean.Data> = ObservableField()

    constructor(testViewModel: TestViewModel, data: ArticleBean.Data) : this(testViewModel) {
        entity.set(data)
    }

    var itemOnClick: BindingCommand<Any?> = BindingCommand(BindingAction {
        viewModel.uc.deleteItemLiveData.value = this
        ToastHelper.showNormalToast("${entity.get()?.title},下标=${viewModel.getItemPosition(this)}")
    })


}