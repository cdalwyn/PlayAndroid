package com.czl.module_user.viewmodel

import android.content.Intent
import com.afollestad.materialdialogs.utils.MDUtil.getStringArray
import com.czl.lib_base.annotation.TodoPriority
import com.czl.lib_base.annotation.TodoType
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.TodoBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_user.R


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class TodoInfoFmViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()

    @TodoType
    var todoType: Int = 0

    @TodoPriority
    var todoPriority: Int = 0

    class UiChangeEvent {
        val pickDateEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val saveDataEvent:SingleLiveEvent<Void> = SingleLiveEvent()
        val updateSuccessEvent:SingleLiveEvent<TodoBean.Data> = SingleLiveEvent()
    }

    val onTypeGroupCheckCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        todoType = application.getStringArray(R.array.todo_type).indexOf(it)
    })

    val onPriorityCheckCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        todoPriority = application.getStringArray(R.array.todo_priority).indexOf(it) + 1
    })

    val onDateClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.pickDateEvent.call()
    })

    val onSaveClickCommand:BindingCommand<Void> = BindingCommand(BindingAction {
        uc.saveDataEvent.call()
    })

    fun saveData(todoInfo:TodoBean.Data){
        model.updateTodo(todoInfo)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading() }
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                override fun onResult(t: BaseBean<Any?>) {
                    dismissLoading()
                    if (t.errorCode == 0) {
                        showSuccessToast("保存成功")
                        uc.updateSuccessEvent.postValue(todoInfo)
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                }
            })
    }

}