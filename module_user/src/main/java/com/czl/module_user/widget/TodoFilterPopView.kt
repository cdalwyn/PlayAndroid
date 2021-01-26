package com.czl.module_user.widget

import android.annotation.SuppressLint
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.utils.MDUtil.getStringArray
import com.blankj.utilcode.util.StringUtils
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.module_user.R
import com.czl.module_user.databinding.UserPopFilterBinding
import com.czl.module_user.ui.fragment.UserTodoFragment
import com.lxj.xpopup.core.DrawerPopupView

/**
 * @author Alwyn
 * @Date 2021/1/19
 * @Description
 */
@SuppressLint("ViewConstructor")
class TodoFilterPopView(
    private val mFragment: UserTodoFragment,
    private val status: Int,
    private val todoType: Int,
    private val priority: Int,
    private val timeState: Int
) : DrawerPopupView(mFragment.requireContext()) {
    private var dataBinding: UserPopFilterBinding? = null
    private var mType = -1
    private var mStatus = 0
    private var mPriority = 0

    override fun getImplLayoutId(): Int {
        return R.layout.user_pop_filter
    }

    override fun onCreate() {
        super.onCreate()
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.apply {
            pop = this@TodoFilterPopView
            status = this@TodoFilterPopView.status
            type = todoType
            priority = this@TodoFilterPopView.priority
            timeState = this@TodoFilterPopView.timeState
            executePendingBindings()
        }
    }

    val onConfirmClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.viewModel.apply {
            todoType = mType
            priority = mPriority
            status = mStatus
        }
        mFragment.binding.smartCommon.autoRefresh()
        dismiss()
    })

    val onTypeGroupCheckCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        if (it == "全部") {
            mType = 0
            return@BindingConsumer
        }
        mType = StringUtils.getStringArray(R.array.todo_type).indexOf(it)
    })

    val onPriorityGroupCheckCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        mPriority = StringUtils.getStringArray(R.array.todo_priority).indexOf(it) + 1
    })

    val onStatusGroupCheckCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        mStatus = StringUtils.getStringArray(R.array.todo_status).indexOf(it) - 1
    })

    val onTimeStateGroupCheckCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        mFragment.viewModel.timeState =
            StringUtils.getStringArray(R.array.todo_time_state).indexOf(it)
    })
}