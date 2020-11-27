package com.czl.module_main.viewmodel

import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.HomeArticleBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import io.reactivex.Observable

/**
 * @author Alwyn
 * @Date 2020/11/27
 * @Description
 */
class HomeArticleVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentArticlePage:Int = 0
    val loadCompleteEvent:SingleLiveEvent<HomeArticleBean> = SingleLiveEvent()

    override fun refreshCommand() {
        currentArticlePage = -1
        getArticle()
    }

    override fun loadMoreCommand() {
        getArticle()
    }

    /**
     * 获取热门博文列表
     */
    private fun getArticle() {
        model.getHomeArticle((currentArticlePage + 1).toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<HomeArticleBean>>() {
                override fun onResult(t: BaseBean<HomeArticleBean>) {
                    if (t.errorCode == 0) {
                        currentArticlePage++
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