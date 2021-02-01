package com.czl.module_square.viewmodel

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.bean.SystemDetailBean
import com.czl.lib_base.data.bean.SystemTreeBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import io.reactivex.Observable


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class SystemContentVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentPage = 0
    var cid: String? = null

    val loadCompletedEvent: SingleLiveEvent<SystemDetailBean?> = SingleLiveEvent()

    override fun refreshCommand() {
        currentPage = -1
        getArticlesByCid()
    }

    override fun loadMoreCommand() {
        getArticlesByCid()
    }

    private fun getArticlesByCid() {
        model.getArticlesByCid(currentPage + 1, cid!!)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<SystemDetailBean>>() {
                override fun onResult(t: BaseBean<SystemDetailBean>) {
                    if (t.errorCode == 0) {
                        currentPage++
                        loadCompletedEvent.postValue(t.data)
                    } else {
                        loadCompletedEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                    loadCompletedEvent.postValue(null)
                }

            })
    }


    /**
     * 收藏
     */
    fun collectArticle(id: Int): Observable<BaseBean<Any?>> {
        return model.collectArticle(id).compose(RxThreadHelper.rxSchedulerHelper(this))
    }

    fun unCollectArticle(id: Int): Observable<BaseBean<Any?>> {
        return model.unCollectArticle(id).compose(RxThreadHelper.rxSchedulerHelper(this))
    }
}