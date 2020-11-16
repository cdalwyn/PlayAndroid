package com.czl.module_user.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel
import com.czl.lib_base.data.bean.ArticleBean
import com.czl.lib_base.data.bean.UserScoreDetailBean
import com.czl.lib_base.util.ToastHelper


/**
 * @author Alwyn
 * @Date 2020/7/28
 * @Description
 */
class ScoreItemVm(scoreVm: UserScoreVm) : ItemViewModel<UserScoreVm>(scoreVm) {

    var entity: ObservableField<UserScoreDetailBean.Data> = ObservableField()

    constructor(scoreVm: UserScoreVm, data: UserScoreDetailBean.Data) : this(scoreVm) {
        entity.set(data)
    }

}