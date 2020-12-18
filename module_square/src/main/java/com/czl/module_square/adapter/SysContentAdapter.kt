package com.czl.module_square.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.SystemDetailBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.module_square.R
import com.czl.module_square.databinding.SquareItemArticleBinding
import com.czl.module_square.ui.fragment.SysContentFragment
import com.czl.module_square.ui.fragment.SystemDetailFragment

/**
 * @author Alwyn
 * @Date 2020/12/5
 * @Description
 */
class SysContentAdapter(val mFragment: SysContentFragment) :
    BaseQuickAdapter<SystemDetailBean.Data, BaseDataBindingHolder<SquareItemArticleBinding>>(
        R.layout.square_item_article
    ) {

    val tvShare = " 分享 "
    val tvAuthor = " 作者 "

    override fun convert(
        holder: BaseDataBindingHolder<SquareItemArticleBinding>,
        item: SystemDetailBean.Data
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@SysContentAdapter
            executePendingBindings()
        }
    }

    val onArticleItemClick: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is SystemDetailBean.Data) {
            val bundle = Bundle()
            bundle.putString(AppConstants.BundleKey.WEB_URL, it.link)
            (mFragment.parentFragment as SystemDetailFragment).viewModel.startContainerActivity(AppConstants.Router.Web.F_WEB, bundle)
        }
    })

    val onCollectClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is SystemDetailBean.Data) {
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

    val diffConfig = object : DiffUtil.ItemCallback<SystemDetailBean.Data>() {
        override fun areItemsTheSame(
            oldItem: SystemDetailBean.Data,
            newItem: SystemDetailBean.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SystemDetailBean.Data,
            newItem: SystemDetailBean.Data
        ): Boolean {
            return oldItem.title == newItem.title
        }

    }
}