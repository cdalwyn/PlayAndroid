package com.czl.module_user.viewmodel

import androidx.databinding.ObservableBoolean
import com.blankj.utilcode.util.AppUtils
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.db.WebHistoryEntity
import com.czl.lib_base.util.DayModeUtil
import com.czl.lib_base.util.RxThreadHelper

/**
 * @author Alwyn
 * @Date 2020/12/7
 * @Description
 */
class UserSettingVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()
    val followSysUiModeState: ObservableBoolean = ObservableBoolean(false)

    class UiChangeEvent {
        val switchUiModeEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
        val switchSysModeEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    }

    val onFollowSysModeCheckedCommand: BindingCommand<Boolean> = BindingCommand { checked ->
        followSysUiModeState.set(checked)
        if (model.getFollowSysUiModeFlag()==checked) return@BindingCommand
        model.saveFollowSysModeFlag(checked)
        uc.switchSysModeEvent.postValue(checked)
    }
    val onSwitchNightModeCheckedCommand: BindingCommand<Boolean> = BindingCommand { checked ->
        // 不随系统模式才发送
        if (!followSysUiModeState.get()) {
            model.saveUiMode(checked)
            uc.switchUiModeEvent.postValue(checked)
        }
    }
}