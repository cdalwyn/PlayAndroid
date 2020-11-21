package com.czl.module_user.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.data.bean.CollectWebsiteBean
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemWebsiteBinding
import com.czl.module_user.ui.fragment.CollectWebsiteFragment


/**
 * @author Alwyn
 * @Date 2020/11/18
 * @Description
 */
class UserCollectWebAdapter(val mFragment: CollectWebsiteFragment) :
    BaseQuickAdapter<CollectWebsiteBean, BaseDataBindingHolder<UserItemWebsiteBinding>>(
        R.layout.user_item_website
    ) {

    override fun convert(
        holder: BaseDataBindingHolder<UserItemWebsiteBinding>,
        item: CollectWebsiteBean
    ) {

        holder.dataBinding?.apply {
            data = item
            menu = holder.dataBinding?.menuLayout
            executePendingBindings()
        }
    }

    val diffConfig = object : DiffUtil.ItemCallback<CollectWebsiteBean>() {
        override fun areItemsTheSame(
            oldItem: CollectWebsiteBean,
            newItem: CollectWebsiteBean
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: CollectWebsiteBean,
            newItem: CollectWebsiteBean
        ): Boolean {
            return oldItem.link == newItem.link
        }

    }
}