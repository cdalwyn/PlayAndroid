package com.czl.module_square.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.bean.SystemTreeBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class SystemDetailVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val tvToolbarTitle = ObservableField("")

    val loadCompletedEvent: SingleLiveEvent<List<SystemTreeBean>?> = SingleLiveEvent()

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {

    })
}