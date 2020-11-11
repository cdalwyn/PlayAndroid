package com.czl.module_main.viewmodel

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.HomeArticleBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel
import com.czl.module_main.R



/**
 * @author Alwyn
 * @Date 2020/10/30
 * @Description
 */
class HomeArticleItemVm(homeViewModel: HomeViewModel) :
    ItemViewModel<HomeViewModel>(homeViewModel) {
    var entity: ObservableField<HomeArticleBean.Data> = ObservableField()
    // 绑定收藏图标
    var ivCollect : ObservableInt = ObservableInt(R.drawable.ic_like_off_gray)

    val tvShare = " 分享 "
    val tvAuthor = " 作者 "

    constructor(homeViewModel: HomeViewModel, data: HomeArticleBean.Data) : this(homeViewModel) {
        entity.set(data)
        // 判断收藏状态
        if (data.collect){
            ivCollect.set(R.drawable.ic_like_on)
        }
    }

    val onArticleItemClick: BindingCommand<Void> = BindingCommand(BindingAction {
        val bundle = Bundle()
        bundle.putString(AppConstants.BundleKey.WEB_URL, this.entity.get()?.link)
        viewModel.startContainerActivity(AppConstants.Router.Base.F_WEB, bundle)
    })

    val onCollectClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        val data = entity.get()
        data?.let {
            if (!data.collect) {
                homeViewModel.collectArticle(data.id)
                    .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                        override fun onResult(t: BaseBean<*>) {
                            if (t.errorCode == 0) {
                                viewModel.showSuccessToast("收藏成功")
                                data.collect = true
                                ivCollect.set(R.drawable.ic_like_on)
                            } else {
                                viewModel.showErrorToast(t.errorMsg)
                            }
                        }

                        override fun onFailed(msg: String?) {
                            viewModel.showErrorToast(msg)
                        }

                    })
            } else {
                homeViewModel.unCollectArticle(data.id)
                    .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                        override fun onResult(t: BaseBean<*>) {
                            if (t.errorCode == 0) {
                                data.collect = false
                                ivCollect.set(R.drawable.ic_like_off_gray)
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
    })

}