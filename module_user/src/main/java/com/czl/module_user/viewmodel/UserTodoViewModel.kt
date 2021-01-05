package com.czl.module_user.viewmodel

import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.event.LiveBusCenter


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class UserTodoViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    override fun refreshCommand() {

    }
}