package com.czl.module_square.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.CollectArticleBean
import com.czl.lib_base.data.bean.SquareListBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.module_square.R
import com.czl.module_square.databinding.SquareItemHomeBinding
import com.czl.module_square.ui.fragment.SquareFragment

/**
 * @author Alwyn
 * @Date 2020/11/28
 * @Description
 */
class SquareHomeAdapter(val mFragment: SquareFragment) :
    BaseQuickAdapter<SquareListBean.Data, BaseDataBindingHolder<SquareItemHomeBinding>>(
        R.layout.square_item_home
    ) {
    val tvShare = " 分享 "
    val tvAuthor = " 作者 "
    override fun convert(
        holder: BaseDataBindingHolder<SquareItemHomeBinding>,
        item: SquareListBean.Data
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@SquareHomeAdapter
            executePendingBindings()
        }
    }

    val onItemClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is SquareListBean.Data) {
            mFragment.startContainerActivity(AppConstants.Router.Base.F_WEB, Bundle().apply {
                putString(AppConstants.BundleKey.WEB_URL, it.link)
            })
        }
    })
    val onCollectClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is SquareListBean.Data) {
            if (!it.collect) {
                mFragment.viewModel.collectArticle(it.id)
                    .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                        override fun onResult(t: BaseBean<*>) {
                            if (t.errorCode == 0) {
                                LiveBusCenter.postRefreshUserFmEvent()
                                mFragment.showSuccessToast("收藏成功")
                                it.collect = true
                            } else {
                                mFragment.showErrorToast(t.errorMsg)
                            }
                        }

                        override fun onFailed(msg: String?) {
                            mFragment.showErrorToast(msg)
                        }
                    })
            } else {
                mFragment.viewModel.unCollectArticle(it.id)
                    .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                        override fun onResult(t: BaseBean<*>) {
                            if (t.errorCode == 0) {
                                LiveBusCenter.postRefreshUserFmEvent()
                                it.collect = false
                            } else {
                                mFragment.showErrorToast(t.errorMsg)
                            }
                        }

                        override fun onFailed(msg: String?) {
                            mFragment.showErrorToast(msg)
                        }
                    })
            }
        }
    })

    val diffConfig = object : DiffUtil.ItemCallback<SquareListBean.Data>() {
        override fun areItemsTheSame(
            oldItem: SquareListBean.Data,
            newItem: SquareListBean.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SquareListBean.Data,
            newItem: SquareListBean.Data
        ): Boolean {
            return oldItem.title == newItem.title
        }

    }
}