package com.czl.module_main.viewmodel

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.data.net.RetrofitClient
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.extension.ImagePopLoader
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel
import com.czl.module_main.R
import com.czl.module_main.widget.ProjectItemSettingPop
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupCallback
import com.czl.lib_base.binding.command.BindingCommand


/**
 * @author Alwyn
 * @Date 2020/10/30
 * @Description
 */
class HomeProjectItemVm(homeViewModel: HomeViewModel) :
    ItemViewModel<HomeViewModel>(homeViewModel) {
    var entity: ObservableField<ProjectBean.Data> = ObservableField()

    constructor(homeViewModel: HomeViewModel, data: ProjectBean.Data) : this(homeViewModel) {
        entity.set(data)
    }

    /*Item点击事件*/
    val onProjectItemClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        val bundle = Bundle()
        bundle.putString(AppConstants.BundleKey.WEB_URL, this.entity.get()?.link)
        viewModel.startContainerActivity(AppConstants.Router.Base.F_WEB, bundle)
    })

    /*Item的设置点击事件*/
    val onItemSettingClickCommand: View.OnClickListener = View.OnClickListener { view ->
        XPopup.Builder(view.context)
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
                    val ivCollect = popupView.findViewById<ImageView>(R.id.iv_collect)
                    if (entity.get()!!.collect) {
                        ivCollect.setImageResource(R.drawable.ic_like_on)
                        tvCollect.text = view.context.getString(R.string.main_cancel_collect)
                    } else {
                        ivCollect.setImageResource(R.drawable.ic_like_off)
                        tvCollect.text = view.context.getString(R.string.main_collect)
                    }
                    tvCollect.setOnClickListener {
                        if (!entity.get()!!.collect) {
                            // 收藏
                            homeViewModel.collectArticle(entity.get()!!.id)
                                .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                                    override fun onResult(t: BaseBean<*>) {
                                        if (t.errorCode == 0) {
                                            entity.get()?.collect = true
                                            viewModel.showSuccessToast("收藏成功")
                                            ivCollect.setImageResource(R.drawable.ic_like_on)
                                            tvCollect.text =
                                                it.context.getString(R.string.main_cancel_collect)
                                        } else {
                                            viewModel.showErrorToast(t.errorMsg)
                                        }
                                    }

                                    override fun onFailed(msg: String?) {
                                        viewModel.showErrorToast(msg)
                                    }
                                })
                        } else {
                            // 取消收藏
                            homeViewModel.unCollectArticle(entity.get()!!.id)
                                .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                                    override fun onResult(t: BaseBean<*>) {
                                        if (t.errorCode == 0) {
                                            entity.get()?.collect = false
                                            ivCollect.setImageResource(R.drawable.ic_like_off)
                                            tvCollect.text =
                                                it.context.getString(R.string.main_collect)
                                        } else {
                                            viewModel.showErrorToast(t.errorMsg)
                                        }
                                    }

                                    override fun onFailed(msg: String?) {
                                        viewModel.showErrorToast(msg)
                                    }

                                })
                        }

                    }
                    popupView.findViewById<TextView>(R.id.tv_same)
                        .setOnClickListener {
                            // 查相似
                            val bundle = Bundle()
                            bundle.putString(AppConstants.BundleKey.WEB_URL,RetrofitClient.baseUrl+entity.get()!!.tags[0].url)
                            homeViewModel.startContainerActivity(AppConstants.Router.Base.F_WEB,bundle)
                            popupView.dismiss()
                        }
                }

                override fun beforeDismiss(popupView: BasePopupView?) {
                }

                override fun onShow(popupView: BasePopupView?) {
                }
            })
            .atView(view)
            .asCustom(ProjectItemSettingPop(view.context))
            .show()
    }

    /*Item的图片点击事件*/
    val onItemPicClickCommand: View.OnClickListener = View.OnClickListener {
        XPopup.Builder(it.context)
            .asImageViewer(it as ImageView, this.entity.get()?.envelopePic, ImagePopLoader())
            .show()
    }


}