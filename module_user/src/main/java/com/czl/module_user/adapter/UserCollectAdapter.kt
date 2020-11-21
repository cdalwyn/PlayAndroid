package com.czl.module_user.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.CollectArticleBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemCollectBinding
import com.czl.module_user.ui.fragment.CollectArticleFragment

/**
 * @author Alwyn
 * @Date 2020/11/18
 * @Description
 */
class UserCollectAdapter(val mFragment: CollectArticleFragment) :
    BaseQuickAdapter<CollectArticleBean.Data, BaseDataBindingHolder<UserItemCollectBinding>>(
        R.layout.user_item_collect
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<UserItemCollectBinding>,
        item: CollectArticleBean.Data
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@UserCollectAdapter
            lifecycleOwner = mFragment.viewLifecycleOwner
            executePendingBindings()
        }
    }

    val onItemClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is CollectArticleBean.Data)
            mFragment.startContainerActivity(AppConstants.Router.Base.F_WEB, Bundle().apply {
                putString(AppConstants.BundleKey.WEB_URL,it.link)
            })
    })

    val onDisCollectCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is CollectArticleBean.Data) {
            mFragment.viewModel.model.unCollectArticle(
                it.id,
                if (it.originId == 0) -1 else it.originId
            )
                .compose(RxThreadHelper.rxSchedulerHelper(mFragment.viewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        if (t.errorCode == 0) {
                            remove(it)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        mFragment.showErrorToast(msg)
                    }

                })
        }
    })

    val diffConfig = object : DiffUtil.ItemCallback<CollectArticleBean.Data>() {
        override fun areItemsTheSame(
            oldItem: CollectArticleBean.Data,
            newItem: CollectArticleBean.Data
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: CollectArticleBean.Data,
            newItem: CollectArticleBean.Data
        ): Boolean {
            return oldItem.title == newItem.title
        }

    }
}