package com.czl.module_user.viewmodel

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.bean.UserShareBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class UserShareVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentPage = 1

    val loadDataCompleteEvent: SingleLiveEvent<UserShareBean.ShareArticles?> = SingleLiveEvent()

    override fun refreshCommand() {
        currentPage = 0
        getUserShareData()
    }

    override fun loadMoreCommand() {
        getUserShareData()
    }

    fun getUserShareData() {
        model.getUserShareData((currentPage + 1).toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<UserShareBean>>() {
                override fun onResult(t: BaseBean<UserShareBean>) {
                    if (t.errorCode == 0) currentPage++
                    loadDataCompleteEvent.postValue(t.data?.shareArticles)
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                    loadDataCompleteEvent.postValue(null)
                }
            })
    }
}