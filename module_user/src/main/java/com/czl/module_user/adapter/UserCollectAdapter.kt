package com.czl.module_user.adapter

import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.data.bean.CollectArticleBean
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemCollectBinding

/**
 * @author Alwyn
 * @Date 2020/11/18
 * @Description
 */
class UserCollectAdapter :
    BaseQuickAdapter<CollectArticleBean.Data, BaseDataBindingHolder<UserItemCollectBinding>>(
        R.layout.user_item_collect
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<UserItemCollectBinding>,
        item: CollectArticleBean.Data
    ) {
        holder.dataBinding?.data = item

    }
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