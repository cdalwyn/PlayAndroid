package com.czl.module_user.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.DiffUtil
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.annotation.TodoPriority
import com.czl.lib_base.annotation.TodoType
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.TodoBean
import com.czl.lib_base.widget.EasySwipeMenuLayout
import com.czl.module_user.R
import com.czl.module_user.databinding.UserItemTodoBinding
import com.czl.module_user.ui.fragment.UserTodoFragment
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Alwyn
 * @Date 2021/1/6
 * @Description
 */
class UserTodoAdapter(private val mFragment: UserTodoFragment) :
    BaseQuickAdapter<TodoBean.Data, BaseDataBindingHolder<UserItemTodoBinding>>(R.layout.user_item_todo) {
    override fun convert(holder: BaseDataBindingHolder<UserItemTodoBinding>, item: TodoBean.Data) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@UserTodoAdapter
            when (item.priority) {
                TodoPriority.HIGH -> {
                    tvPriority.text = "高"
                    tvPriority.setTextColor(ContextCompat.getColor(context, R.color.md_theme_red))
                    tvPriority.setBackgroundResource(R.drawable.stroke_red)
                }
                TodoPriority.LOW -> {
                    tvPriority.text = "低"
                    tvPriority.setTextColor(Color.parseColor("#1473E6"))
                    tvPriority.setBackgroundResource(R.drawable.stroke_blue)
                }
                TodoPriority.NORMAL -> {
                    tvPriority.text = "正常"
                    tvPriority.setTextColor(ContextCompat.getColor(context, R.color.green_68))
                    tvPriority.setBackgroundResource(R.drawable.stroke_green)
                }
            }
            when (item.type) {
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
        // 若未完成且日期已过期
//        if (item.status == 0 && item.date < nowTimeMills && !TimeUtils.isToday(item.date)) {
//            holder.dataBinding?.ivState?.apply {
//                visibility = View.VISIBLE
//                setImageResource(R.drawable.ic_date_out)
//            }
//        }
    }

    val onItemClickCommand: BindingCommand<Any?> = BindingCommand(BindingConsumer {
        if (it is TodoBean.Data) {
            mFragment.startContainerActivity(
                AppConstants.Router.User.F_USER_TODO_INFO,
                Bundle().apply {
                    putParcelable(AppConstants.BundleKey.TODO_INFO_DATA, it)
                },201)
        }
    })

    val onFinishClickCommand: BindingCommand<Any?> = BindingCommand(BindingConsumer {
        if (it is TodoBean.Data) {
            val itemPosition = getItemPosition(it)
            mFragment.viewModel.updateTodoState(it.id, if (it.status == 0) 1 else 0) {
                (getViewByPosition(itemPosition, R.id.swipe_layout) as EasySwipeMenuLayout).resetStatus()
                // 标记已完成或者未完成
                it.status = if (it.status == 1) 0 else 1
                (getViewByPosition(itemPosition, R.id.iv_state) as ImageView).setImageDrawable(getImage(it))
            }
        }
    })

    val onDeleteClickCommand: BindingCommand<Any?> = BindingCommand(BindingConsumer {
        if (it is TodoBean.Data) {
            mFragment.viewModel.deleteTodo(it.id) {
                remove(it)
            }
        }
    })

    val diffConfig = object : DiffUtil.ItemCallback<TodoBean.Data>() {
        override fun areItemsTheSame(
            oldItem: TodoBean.Data,
            newItem: TodoBean.Data
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TodoBean.Data,
            newItem: TodoBean.Data
        ): Boolean {
            return oldItem == newItem
        }
    }

    fun getImage(item:TodoBean.Data):Drawable?{
        if (item.status==1){
            return ContextCompat.getDrawable(context,R.drawable.ic_finished_flag)
        }
        if (item.dateExpired){
            return ContextCompat.getDrawable(context,R.drawable.ic_date_out)
        }
        return null
    }
}