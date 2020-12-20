package com.czl.module_user.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.CollectArticleBean
import com.czl.lib_base.data.bean.HomeArticleBean
import com.czl.lib_base.data.bean.ShareUserDetailBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemArticleBinding
import com.czl.module_user.ui.fragment.ShareUserDetailFragment

/**
 * @author Alwyn
 * @Date 2020/12/19
 * @Description
 */
class UserDetailAdapter(private val mFragment: ShareUserDetailFragment) :
    BaseQuickAdapter<ShareUserDetailBean.ShareArticles.Data, BaseDataBindingHolder<UserItemArticleBinding>>(
        R.layout.user_item_article
    ) {
    val tvShare = " 分享 "
    val tvAuthor = " 作者 "
    override fun convert(
        holder: BaseDataBindingHolder<UserItemArticleBinding>,
        item: ShareUserDetailBean.ShareArticles.Data
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@UserDetailAdapter
            executePendingBindings()
        }
    }

    val onArticleItemClick: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is ShareUserDetailBean.ShareArticles.Data) {
            val bundle = Bundle()
            bundle.putString(AppConstants.BundleKey.WEB_URL, it.link)
            mFragment.viewModel.startContainerActivity(AppConstants.Router.Web.F_WEB, bundle)
        }
    })
    val onCollectClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is ShareUserDetailBean.ShareArticles.Data) {
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
    val diffConfig = object : DiffUtil.ItemCallback<ShareUserDetailBean.ShareArticles.Data>() {
        override fun areItemsTheSame(
            oldItem: ShareUserDetailBean.ShareArticles.Data,
            newItem: ShareUserDetailBean.ShareArticles.Data
        ): Boolean {
            return oldItem.userId == newItem.userId && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShareUserDetailBean.ShareArticles.Data,
            newItem: ShareUserDetailBean.ShareArticles.Data
        ): Boolean {
            return oldItem.title == newItem.title && oldItem.link == newItem.link
        }

    }


}