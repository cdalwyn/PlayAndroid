package com.czl.module_square.viewmodel

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.event.SingleLiveEvent
import com.czl.lib_base.data.bean.NavigationBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class NavigateViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val loadCompleteEvent: SingleLiveEvent<List<NavigationBean>?> = SingleLiveEvent()

    override fun refreshCommand() {
        model.getNavData()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object :ApiSubscriberHelper<BaseBean<List<NavigationBean>>>(loadService){
                override fun onResult(t: BaseBean<List<NavigationBean>>) {
                    if (t.errorCode==0){
                        loadCompleteEvent.postValue(t.data)
                    }else{
                        loadCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                    loadCompleteEvent.postValue(null)
                }
            })
    }

}