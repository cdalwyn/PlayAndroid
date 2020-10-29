package com.czl.module_main.viewmodel

import androidx.fragment.app.Fragment
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.event.callback.UIChangeLiveData
import com.czl.lib_base.route.RouteCenter
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.binding.command.BindingConsumer
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent
import me.goldze.mvvmhabit.utils.ToastUtils
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author Alwyn
 * @Date 2020/10/29
 * @Description
 */
class MainViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    val uc = UiChangeEvent()

    inner class UiChangeEvent {
        val tabChangeLiveEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val pageChangeLiveEvent: SingleLiveEvent<Int> = SingleLiveEvent()
    }

    val onTabSelectedListener: BindingCommand<Int> = BindingCommand(BindingConsumer {
        uc.tabChangeLiveEvent.postValue(it)
    })

    val onPageSelectedListener: BindingCommand<Int> = BindingCommand(BindingConsumer {
        uc.pageChangeLiveEvent.postValue(it)
    })

    override fun setToolbarRightClickListener(): () -> Unit {
        return {
            startContainerActivity(AppConstants.Router.Search.F_SEARCH)
        }
    }
}