package com.czl.module_user.viewmodel

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.bean.CollectArticleBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class CollectArticleVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentPage = 0

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val refreshCompleteEvent: SingleLiveEvent<CollectArticleBean?> = SingleLiveEvent()
    }

    override fun loadMoreCommand() {
        currentPage += 1
        getUserCollectData()
    }

    override fun refreshCommand() {
        currentPage = 0
        getUserCollectData()
    }

    fun getUserCollectData() {
        model.getCollectArticle(currentPage.toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<CollectArticleBean>>() {
                override fun onResult(t: BaseBean<CollectArticleBean>) {
                    if (t.errorCode == 0) {
                        uc.refreshCompleteEvent.postValue(t.data)
                    } else {
                        if (currentPage > 0) currentPage -= 1
                        uc.refreshCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    if (currentPage > 0) currentPage -= 1
                    showErrorToast(msg)
                    uc.refreshCompleteEvent.postValue(null)
                }

            })
    }
}