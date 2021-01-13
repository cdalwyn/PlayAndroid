package com.czl.module_user.viewmodel

import com.afollestad.materialdialogs.utils.MDUtil.getStringArray
import com.czl.lib_base.annotation.TodoPriority
import com.czl.lib_base.annotation.TodoType
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
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


}