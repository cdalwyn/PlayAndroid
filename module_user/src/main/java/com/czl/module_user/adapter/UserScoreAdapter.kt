package com.czl.module_user.adapter

import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.data.bean.UserScoreBean
import com.czl.lib_base.data.bean.UserScoreDetailBean
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemScoreBinding

/**
 * @author Alwyn
 * @Date 2020/11/16
 * @Description
 */
class UserScoreAdapter :
    BaseQuickAdapter<UserScoreDetailBean.Data, BaseDataBindingHolder<UserItemScoreBinding>>(R.layout.user_item_score) {
    override fun convert(
        holder: BaseDataBindingHolder<UserItemScoreBinding>,
        item: UserScoreDetailBean.Data
    ) {
        holder.dataBinding?.data = item
        holder.dataBinding?.executePendingBindings()
    }
    val diffConfig = object : DiffUtil.ItemCallback<UserScoreDetailBean.Data>() {
        override fun areItemsTheSame(
            oldItem: UserScoreDetailBean.Data,
            newItem: UserScoreDetailBean.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserScoreDetailBean.Data,
            newItem: UserScoreDetailBean.Data
        ): Boolean {
            return oldItem.desc == newItem.desc
        }

    }
}