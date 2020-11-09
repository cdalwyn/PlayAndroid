package com.czl.module_search.viewmodel

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.SearchDataBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.mvvm.viewmodel.ItemViewModel
import com.czl.lib_base.route.RouteCenter
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_search.R
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author Alwyn
 * @Date 2020/11/6
 * @Description
 */
class SearchItemViewModel(searchViewModel: SearchViewModel) :
    ItemViewModel<SearchViewModel>(searchViewModel) {
    var entity: ObservableField<SearchDataBean.Data> = ObservableField()
    val tvShare = "分享者："
    val tvAuthor = "作者："

    // 绑定收藏图标
    var ivCollect : ObservableInt = ObservableInt(R.drawable.ic_like_off_gray)

    constructor(
        searchViewModel: SearchViewModel,
        data: SearchDataBean.Data
    ) : this(searchViewModel) {
        entity.set(data)
        // 判断收藏状态
        if (data.collect){
            ivCollect.set(R.drawable.ic_like_on)
        }
    }

    val searchItemClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        val bundle = Bundle()
        bundle.putString(AppConstants.BundleKey.WEB_URL, this.entity.get()?.link)
        viewModel.startFragment(RouteCenter.navigate(AppConstants.Router.Base.F_WEB) as SupportFragment, bundle)
    })

    val collectClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        val data = entity.get()
        data?.let {
            if (!data.collect) {
                searchViewModel.model.collectArticle(data.id)
                    .compose(RxThreadHelper.rxSchedulerHelper(searchViewModel))
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
                searchViewModel.model.unCollectArticle(data.id)
                    .compose(RxThreadHelper.rxSchedulerHelper(searchViewModel))
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