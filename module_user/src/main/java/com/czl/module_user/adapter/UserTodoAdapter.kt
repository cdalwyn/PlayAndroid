package com.czl.module_user.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.annotation.TodoPriority
import com.czl.lib_base.annotation.TodoType
import com.czl.lib_base.data.bean.TodoBean
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemTodoBinding
import com.czl.module_user.ui.fragment.UserTodoFragment

/**
 * @author Alwyn
 * @Date 2021/1/6
 * @Description
 */
class UserTodoAdapter(val userTodoFragment: UserTodoFragment) :
    BaseQuickAdapter<TodoBean.Data, BaseDataBindingHolder<UserItemTodoBinding>>(R.layout.user_item_todo) {
    override fun convert(holder: BaseDataBindingHolder<UserItemTodoBinding>, item: TodoBean.Data) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@UserTodoAdapter
            when(item.priority){
                TodoPriority.HIGH -> {
                    tvPriority.text = "高"
                    tvPriority.setTextColor(ContextCompat.getColor(context,R.color.md_theme_red))
                    tvPriority.setBackgroundResource(R.drawable.stroke_red)
                }
                TodoPriority.LOW -> {
                    tvPriority.text = "低"
                    tvPriority.setTextColor(Color.parseColor("#1473E6"))
                    tvPriority.setBackgroundResource(R.drawable.stroke_blue)
                }
                TodoPriority.NORMAL -> {
                    tvPriority.text = "正常"
                    tvPriority.setTextColor(ContextCompat.getColor(context,R.color.green_68))
                    tvPriority.setBackgroundResource(R.drawable.stroke_green)
                }
            }
            when(item.type){
                TodoType.ALL -> {
                   ivType.setImageResource(R.drawable.ic_todo_white)
                }
                TodoType.FAMILY -> {
                    ivType.setImageResource(R.drawable.ic_family)
                }
                TodoType.LIFE -> {
                    ivType.setImageResource(R.drawable.ic_life)
                }
                TodoType.PAY -> {
                    ivType.setImageResource(R.drawable.ic_pay)
                }
                TodoType.PLAY -> {
                    ivType.setImageResource(R.drawable.ic_play)
                }
                TodoType.STUDY -> {
                    ivType.setImageResource(R.drawable.ic_study)
                }
                TodoType.WORK -> {
                    ivType.setImageResource(R.drawable.ic_work)
                }
            }
            executePendingBindings()
        }
    }
    val diffConfig = object : DiffUtil.ItemCallback<TodoBean.Data>() {
        override fun areItemsTheSame(
            oldItem: TodoBean.Data,
            newItem: TodoBean.Data
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: TodoBean.Data,
            newItem: TodoBean.Data
        ): Boolean {
            return oldItem == newItem
        }
    }
}