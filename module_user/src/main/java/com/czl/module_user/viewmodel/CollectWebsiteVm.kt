package com.czl.module_user.viewmodel

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.CollectWebsiteBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper


/**
 * @author Alwyn
 * @Date 2020/11/18
 * @Description
 */
class CollectWebsiteVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val loadDataCompleteEvent: SingleLiveEvent<List<CollectWebsiteBean>?> = SingleLiveEvent()

    override fun refreshCommand() {
        getCollectWebsite()
    }

    fun getCollectWebsite() {
        model.getUserCollectWebsite()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<CollectWebsiteBean>>>(loadService) {
                override fun onResult(t: BaseBean<List<CollectWebsiteBean>>) {
                    if (t.errorCode == 0) {
                        loadDataCompleteEvent.postValue(t.data)
                    } else {
                        loadDataCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    loadDataCompleteEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }
}