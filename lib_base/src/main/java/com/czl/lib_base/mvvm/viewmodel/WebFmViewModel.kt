package com.czl.lib_base.mvvm.viewmodel

import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository

/**
 * @author Alwyn
 * @Date 2020/10/31
 * @Description
 */
class WebFmViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val closeEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    override fun setToolbarRightClick() {
        uc.closeEvent.call()
    }

    val onCollectClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        showNormalToast("收藏")
    })
    val onMenuClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        showNormalToast("菜单")
    })
}