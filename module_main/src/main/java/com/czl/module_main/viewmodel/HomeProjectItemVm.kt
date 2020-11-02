package com.czl.module_main.viewmodel

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.entity.HomeProjectBean
import com.czl.lib_base.extension.ImagePopLoader
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel
import com.czl.module_main.R
import com.czl.module_main.widget.ProjectItemSettingPop
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupCallback
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
        XPopup.Builder(it.context)
            .hasShadowBg(false)
            .setPopupCallback(object : XPopupCallback {
                override fun onBackPressed(popupView: BasePopupView?): Boolean {
                    return false
                }

                override fun onDismiss(popupView: BasePopupView?) {

                }

                override fun onKeyBoardStateChanged(popupView: BasePopupView?, height: Int) {
                }

                override fun beforeShow(popupView: BasePopupView?) {

                }

                override fun onCreated(popupView: BasePopupView) {
                    val tvCollect = popupView.findViewById<TextView>(R.id.tv_collect)
                    if (entity.get()!!.collect) {
                        popupView.findViewById<ImageView>(R.id.iv_collect)
                            .setImageResource(R.drawable.ic_like_on)
                        tvCollect.text = "取消收藏"
                    } else {
                        popupView.findViewById<ImageView>(R.id.iv_collect)
                            .setImageResource(R.drawable.ic_like_off)
                        tvCollect.text = "收藏"
                    }
                    tvCollect.setOnClickListener {
                        // 收藏/取消收藏
                        homeViewModel.collectArticle(entity.get()!!.id)
                        popupView.dismiss()
                    }
                    popupView.findViewById<TextView>(R.id.tv_same)
                        .setOnClickListener {
                            // 查相似
                            popupView.dismiss()
                        }
                }

                override fun beforeDismiss(popupView: BasePopupView?) {

                }

                override fun onShow(popupView: BasePopupView?) {

                }

            })
            .atView(it)
            .asCustom(ProjectItemSettingPop(it.context))
            .show()
    }

    /*Item的图片点击事件*/
    val onItemPicClickCommand: View.OnClickListener = View.OnClickListener {
        XPopup.Builder(it.context)
            .asImageViewer(it as ImageView, this.entity.get()?.envelopePic, ImagePopLoader())
            .show()
    }


}