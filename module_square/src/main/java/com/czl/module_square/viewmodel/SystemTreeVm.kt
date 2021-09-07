package com.czl.module_square.viewmodel

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.event.SingleLiveEvent
import com.czl.lib_base.data.bean.SystemTreeBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class SystemTreeVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val loadCompletedEvent: SingleLiveEvent<List<SystemTreeBean>?> = SingleLiveEvent()

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        model.getSystemTreeData()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object :ApiSubscriberHelper<BaseBean<List<SystemTreeBean>>>(loadService){
                override fun onResult(t: BaseBean<List<SystemTreeBean>>) {
                    if (t.errorCode==0){
                        loadCompletedEvent.postValue(t.data)
                    }else{
                        loadCompletedEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                    loadCompletedEvent.postValue(null)
                }
            })
    })
}