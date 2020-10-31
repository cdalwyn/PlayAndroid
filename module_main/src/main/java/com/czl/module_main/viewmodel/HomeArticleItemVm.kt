package com.czl.module_main.viewmodel

import android.os.Bundle
import androidx.databinding.ObservableField
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.entity.ArticleBean
import com.czl.lib_base.data.entity.HomeArticleBean
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel
import com.czl.lib_base.route.RouteCenter
import com.czl.lib_base.util.ToastHelper
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.yokeyword.fragmentation.SupportFragment


/**
 * @author Alwyn
 * @Date 2020/10/30
 * @Description
 */
class HomeArticleItemVm(homeViewModel: HomeViewModel) :
    ItemViewModel<HomeViewModel>(homeViewModel) {
    var entity: ObservableField<HomeArticleBean.Data> = ObservableField()
    val tvShare = "分享者："
    val tvAuthor = "作者："

    constructor(homeViewModel: HomeViewModel, data: HomeArticleBean.Data) : this(homeViewModel) {
        entity.set(data)
    }

    val onArticleItemClick: BindingCommand<Void> = BindingCommand(BindingAction {
        val bundle = Bundle()
        bundle.putString(AppConstants.BundleKey.WEB_URL, this.entity.get()?.link)
        viewModel.startContainerActivity(AppConstants.Router.Base.F_WEB, bundle)
    })

}