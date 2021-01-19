package com.czl.lib_base.widget

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.afollestad.materialdialogs.utils.MDUtil.getStringArray
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.data.bean.TodoBean
import com.czl.lib_base.databinding.PopAddTodoBinding
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.DialogHelper
import com.czl.lib_base.util.RxThreadHelper
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Alwyn
 * @Date 2021/1/8
 * @Description 添加待办的弹窗
 */
@SuppressLint("ViewConstructor")
class AddTodoPopView(val activity: BaseActivity<*, *>) : BottomPopupView(activity) {
    private var dataBinding: PopAddTodoBinding? = null
    private val nowDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val dateObservableStr = ObservableField("今天")
    val priorityObservableStr = ObservableField("正常")
    val typeObservableStr = ObservableField("默认")

    override fun getImplLayoutId(): Int {
        return R.layout.pop_add_todo
    }

    override fun onCreate() {
        super.onCreate()
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.apply {
            pop = this@AddTodoPopView
            clRoot.background = XPopupUtils.createDrawable(
                ContextCompat.getColor(context, R.color.white),
                30f, 30f, 0f, 0f
            )
            initBtnState()
            executePendingBindings()
        }

    }

    private fun PopAddTodoBinding.initBtnState() {
        val titleSubject = PublishSubject.create<String>()
        val contentSubject = PublishSubject.create<String>()
        etTitle.addTextChangedListener(EditTextMonitor(titleSubject))
        etContent.addTextChangedListener(EditTextMonitor(contentSubject))
        btnSend.isEnabled = false
        activity.viewModel.addSubscribe(Observable.combineLatest(
            titleSubject,
            contentSubject,
            { title: String?, content: String? -> !title.isNullOrBlank() && !content.isNullOrBlank() })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                btnSend.isEnabled = it
                btnSend.setImageResource(if (it) R.drawable.ic_send_todo else R.drawable.ic_send_todo_grey)
            })
    }

    val onConfirmClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        dataBinding?.apply {
            val todoTitle = etTitle.text.toString().trim()
            val todoContent = etContent.text.toString().trim()
            val todoDateStr =
                if (dateObservableStr.get() == "今天") nowDateFormat else dateObservableStr.get()
                    ?: nowDateFormat
            val todoType =
                activity.getStringArray(R.array.todo_type).indexOf(typeObservableStr.get())
            val todoPriority = activity.getStringArray(R.array.todo_priority)
                .indexOf(priorityObservableStr.get()) + 1
            activity.dataRepository.addTodo(
                todoTitle, todoContent, todoDateStr, todoType, todoPriority
            ).compose(RxThreadHelper.rxSchedulerHelper(activity.viewModel))
                .subscribe(object : ApiSubscriberHelper<BaseBean<TodoBean.Data>>() {
                    override fun onResult(t: BaseBean<TodoBean.Data>) {
                        if (t.errorCode == 0) {
                            dismiss()
                            activity.showSuccessToast("添加成功")
                            dataBinding?.apply {
                                // 恢复默认状态
                                etTitle.setText("")
                                etContent.setText("")
                                dateObservableStr.set("今天")
                                priorityObservableStr.set("正常")
                                typeObservableStr.set("默认")
                            }
                            t.data?.let {
                                LiveBusCenter.postTodoListRefreshEvent(it.apply {
                                    dateExpired = date < TimeUtils.date2Millis(Date()) && !TimeUtils.isToday(date)
                                })
                            }
                        }
                    }

                    override fun onFailed(msg: String?) {

                    }
                })
        }
    })

    val onDateClickCommand: View.OnClickListener = View.OnClickListener {
        if (it is TextView) {
            // 显示日期选择
            DialogHelper.showDateDialog(
                activity,
                if (it.text.toString() == "今天") nowDateFormat else it.text.toString()
            ) { dialog, date ->
                val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.time)
                dateObservableStr.set(if (dateFormat == nowDateFormat) "今天" else dateFormat)
                dialog.dismiss()
            }
        }
    }

    val onPriorityClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        MaterialDialog(activity).show {
            title(text = "优先级")
            listItemsSingleChoice(
                R.array.todo_priority,
                initialSelection = activity.getStringArray(R.array.todo_priority)
                    .indexOf(priorityObservableStr.get())
            ) { _, _, text ->
                priorityObservableStr.set(text.toString())
            }
            lifecycleOwner(activity)
        }
    })

    val onTypeClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        MaterialDialog(activity).show {
            title(text = "分类")
            listItemsSingleChoice(
                R.array.todo_type,
                initialSelection = activity.getStringArray(R.array.todo_type)
                    .indexOf(typeObservableStr.get())
            ) { _, index, text ->
                typeObservableStr.set(text.toString())
                val drawableStart = when (index) {
                    0 -> ContextCompat.getDrawable(activity, R.drawable.ic_todo_red)
                    1 -> ContextCompat.getDrawable(activity, R.drawable.ic_work_red)
                    2 -> ContextCompat.getDrawable(activity, R.drawable.ic_study_red)
                    3 -> ContextCompat.getDrawable(activity, R.drawable.ic_life_red)
                    4 -> ContextCompat.getDrawable(activity, R.drawable.ic_pay_red)
                    5 -> ContextCompat.getDrawable(activity, R.drawable.ic_play_red)
                    else -> ContextCompat.getDrawable(activity, R.drawable.ic_family_red)
                }
                drawableStart?.setBounds(
                    0, 0, drawableStart.minimumWidth,
                    drawableStart.minimumHeight
                )
                dataBinding?.tvType?.setCompoundDrawables(drawableStart, null, null, null)
            }
            lifecycleOwner(activity)
        }
    })
}