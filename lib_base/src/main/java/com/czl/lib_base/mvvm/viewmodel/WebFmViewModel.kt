package com.czl.lib_base.mvvm.viewmodel

import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository

/**
 * @author Alwyn
 * @Date 2020/10/31
 * @Description
 */
class WebFmViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val closeEvent:SingleLiveEvent<Void> = SingleLiveEvent()
    override fun setToolbarRightClick() {
        closeEvent.call()
    }
}