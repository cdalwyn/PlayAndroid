package com.czl.module_main.viewmodel

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.entity.HomeProjectBean
import com.czl.lib_base.extension.ImagePopLoader
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel
import com.czl.lib_base.util.ToastHelper
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.widget.ProjectItemSettingPop
import com.lxj.xpopup.XPopup
import me.goldze.mvvmhabit.base.BaseModel
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
        val projectItemSettingPop = ProjectItemSettingPop(it.context)
        projectItemSettingPop.post {
            projectItemSettingPop.findViewById<TextView>(R.id.tv_collect)
                .setOnClickListener { projectItemSettingPop.dismiss() }
            projectItemSettingPop.findViewById<TextView>(R.id.tv_same)
                .setOnClickListener { projectItemSettingPop.dismiss() }
        }
        XPopup.Builder(it.context)
            .hasShadowBg(false)
            .atView(it)
            .asCustom(projectItemSettingPop)
            .show()
    }

    /*Item的图片点击事件*/
    val onItemPicClickCommand: View.OnClickListener = View.OnClickListener {
        XPopup.Builder(it.context)
            .asImageViewer(it as ImageView, this.entity.get()?.envelopePic, ImagePopLoader())
            .show()
    }


}