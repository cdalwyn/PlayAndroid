package com.czl.module_user.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.data.bean.UserRankBean
import com.czl.lib_base.data.bean.UserScoreBean
import com.czl.lib_base.data.bean.UserScoreDetailBean
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemRankBinding
import com.czl.module_user.databinding.UserItemScoreBinding

/**
 * @author Alwyn
 * @Date 2020/11/16
 * @Description
 */
class UserRankAdapter :
    BaseQuickAdapter<UserRankBean.Data, BaseDataBindingHolder<UserItemRankBinding>>(R.layout.user_item_rank) {
    override fun convert(
        holder: BaseDataBindingHolder<UserItemRankBinding>,
        item: UserRankBean.Data
    ) {
        holder.dataBinding?.data = item
        when (getItemPosition(item)) {
            0 -> {
                holder.dataBinding?.tvPosition?.apply {
                    text = ""
                    setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_rank_first
                        ), null, null, null
                    )
                }
            }
            1 -> {
                holder.dataBinding?.tvPosition?.apply {
                    text = ""
                    setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_rank_second
                        ), null, null, null
                    )
                }
            }
            2 -> {
                holder.dataBinding?.tvPosition?.apply {
                    text = ""
                    setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_rank_third
                        ), null, null, null
                    )
                }
            }
            else -> {
                holder.dataBinding?.tvPosition?.apply {
                    text = (getItemPosition(item) + 1).toString()
                    setCompoundDrawablesWithIntrinsicBounds(
                        null, null, null, null
                    )
                }
            }
        }
        holder.dataBinding?.executePendingBindings()
    }

    val diffConfig = object : DiffUtil.ItemCallback<UserRankBean.Data>() {
        override fun areItemsTheSame(
            oldItem: UserRankBean.Data,
            newItem: UserRankBean.Data
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: UserRankBean.Data,
            newItem: UserRankBean.Data
        ): Boolean {
            return oldItem.coinCount == newItem.coinCount
        }

    }
}