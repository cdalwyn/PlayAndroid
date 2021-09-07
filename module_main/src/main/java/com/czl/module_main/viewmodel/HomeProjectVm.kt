package com.czl.module_main.viewmodel

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import io.reactivex.Observable

/**
 * @author Alwyn
 * @Date 2020/11/27
 * @Description
 */
class HomeProjectVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentProjectPage = 0
    val loadCompleteEvent: SingleLiveEvent<ProjectBean> = SingleLiveEvent()

    override fun refreshCommand() {
        currentProjectPage = -1
        getProject()
    }

    override fun loadMoreCommand() {
        getProject()
    }

    /**
     * 获取热门项目列表
     */
    private fun getProject() {
        model.getHomeProject((currentProjectPage + 1).toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<ProjectBean>>() {
                override fun onResult(t: BaseBean<ProjectBean>) {
                    if (t.errorCode == 0) {
                        currentProjectPage++
                        loadCompleteEvent.postValue(t.data)
                    } else {
                        loadCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    loadCompleteEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }
    /**
     * 收藏
     */
    fun collectArticle(id: Int): Observable<BaseBean<Any?>> {
        return model.collectArticle(id).compose(RxThreadHelper.rxSchedulerHelper(this))
    }

    /**
     * 取消收藏
     */
    fun unCollectArticle(id: Int): Observable<BaseBean<Any?>> {
        return model.unCollectArticle(id).compose(RxThreadHelper.rxSchedulerHelper(this))
    }
}