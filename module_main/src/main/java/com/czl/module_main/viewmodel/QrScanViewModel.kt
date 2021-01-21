package com.czl.module_main.viewmodel

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository

/**
 * @author Alwyn
 * @Date 2020/10/29
 * @Description
 */
class QrScanViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    val uc = UiChangeEvent()
    val isOpenFlag: ObservableBoolean = ObservableBoolean(false)

    inner class UiChangeEvent {
        val flashLightEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val openAlbumEvent:SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val onFlashLightClick: View.OnClickListener = View.OnClickListener {
        isOpenFlag.set(!isOpenFlag.get())
        uc.flashLightEvent.call()
    }

    override fun setToolbarRightClick() {
        uc.openAlbumEvent.call()
    }
}