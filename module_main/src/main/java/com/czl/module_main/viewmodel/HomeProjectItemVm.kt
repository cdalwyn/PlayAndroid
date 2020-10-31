package com.czl.module_main.viewmodel

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.entity.HomeProjectBean
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand


/**
 * @author Alwyn
 * @Date 2020/10/30
 * @Description
 */
class HomeProjectItemVm(homeViewModel: HomeViewModel) :
    ItemViewModel<HomeViewModel>(homeViewModel) {
    var entity: ObservableField<HomeProjectBean.Data> = ObservableField()
    val tvShare = "分享者："
    val tvAuthor = "作者："

    constructor(homeViewModel: HomeViewModel, data: HomeProjectBean.Data) : this(homeViewModel) {
        entity.set(data)
    }

    /*Item点击事件*/
    val onProjectItemClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        val bundle = Bundle()
        bundle.putString(AppConstants.BundleKey.WEB_URL, this.entity.get()?.link)
        viewModel.startContainerActivity(AppConstants.Router.Base.F_WEB, bundle)
    })

    /*Item的设置点击事件*/
    val onItemSettingClickCommand: View.OnClickListener = View.OnClickListener {
        viewModel.showSettingAttachPop(entity.get()?.id, entity.get()?.chapterId,it)
    }

    /*Item的图片点击事件*/
    val onItemPicClickCommand: View.OnClickListener = View.OnClickListener {
        viewModel.showPicView(it, entity.get()?.envelopePic!!)
    }

}