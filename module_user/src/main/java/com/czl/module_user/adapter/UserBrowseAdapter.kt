package com.czl.module_user.adapter

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.db.WebHistoryEntity
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemBrowseBinding
import com.czl.module_user.ui.fragment.UserBrowseFragment

/**
 * @author Alwyn
 * @Date 2020/12/7
 * @Description
 */
class UserBrowseAdapter(private val mFragment:UserBrowseFragment) :
    BaseQuickAdapter<WebHistoryEntity, BaseDataBindingHolder<UserItemBrowseBinding>>(
        R.layout.user_item_browse
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<UserItemBrowseBinding>,
        item: WebHistoryEntity
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@UserBrowseAdapter
            executePendingBindings()
        }
    }

    val onItemClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is WebHistoryEntity) {
            mFragment.viewModel.startFragment(AppConstants.Router.Web.F_WEB,Bundle().apply {
                putString(AppConstants.BundleKey.WEB_URL,it.webLink)
            })
        }
    })
}