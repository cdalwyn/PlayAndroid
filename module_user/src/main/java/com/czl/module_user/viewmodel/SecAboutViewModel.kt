package com.czl.module_user.viewmodel

import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.event.SingleLiveEvent


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class SecAboutViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val saveWxImgEvent = SingleLiveEvent<Void>()
    val saveAliImgEvent = SingleLiveEvent<Void>()

    val onSaveWxImg: BindingCommand<Void> = BindingCommand(BindingAction {
        saveWxImgEvent.call()
    })

    val onSaveAliImg: BindingCommand<Void> = BindingCommand(BindingAction {
        saveAliImgEvent.call()
    })

}