package com.czl.module_user.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.UserShareBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemShareBinding
import com.czl.module_user.ui.fragment.UserShareFragment

/**
 * @author Alwyn
 * @Date 2020/11/21
 * @Description
 */
class UserShareAdapter(val mFragment: UserShareFragment) :
    BaseQuickAdapter<UserShareBean.ShareArticles.Data, BaseDataBindingHolder<UserItemShareBinding>>(
        R.layout.user_item_share
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<UserItemShareBinding>,
        item: UserShareBean.ShareArticles.Data
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@UserShareAdapter
        }
    }

    val onItemClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is UserShareBean.ShareArticles.Data) {
            mFragment.startContainerActivity(AppConstants.Router.Web.F_WEB, Bundle().apply {
                putString(AppConstants.BundleKey.WEB_URL, it.link)
            })
        }
    })

    val onNameClick:BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is UserShareBean.ShareArticles.Data) {
            mFragment.startContainerActivity(AppConstants.Router.User.F_USER_DETAIL, Bundle().apply {
                putString(AppConstants.BundleKey.USER_ID, it.userId.toString())
            })
        }
    })

    val onDeleteClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is UserShareBean.ShareArticles.Data) {
            mFragment.viewModel.model.deleteArticleById(it.id)
                .compose(RxThreadHelper.rxSchedulerHelper(mFragment.viewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                    override fun onResult(t: BaseBean<Any?>) {
                        if (t.errorCode == 0) {
                            remove(it)
                            mFragment.showSuccessToast("删除成功")
                        }
                    }

                    override fun onFailed(msg: String?) {
                        mFragment.showErrorToast(msg)
                    }
                })
        }
    })

    val diffConfig = object : DiffUtil.ItemCallback<UserShareBean.ShareArticles.Data>() {
        override fun areItemsTheSame(
            oldItem: UserShareBean.ShareArticles.Data,
            newItem: UserShareBean.ShareArticles.Data
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: UserShareBean.ShareArticles.Data,
            newItem: UserShareBean.ShareArticles.Data
        ): Boolean {
            return oldItem.title == newItem.title
        }
    }
}