package com.czl.module_user.viewmodel

import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.event.SingleLiveEvent
import com.czl.lib_base.data.bean.ShareUserDetailBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import io.reactivex.Observable


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class ShareUserDetailVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentPage = -1
    var userId: String? = null
    var userName: String? = null
    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadCompleteEvent: SingleLiveEvent<ShareUserDetailBean?> = SingleLiveEvent()
    }

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = -1
        getUserDetail()
    })
    val onLoadMoreCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        getUserDetail()
    })

    fun getUserDetail() {
        userName?.run {
            model.getArticlesByUserName(currentPage + 1, this)
                .compose(RxThreadHelper.rxSchedulerHelper(this@ShareUserDetailVm))
                .subscribe(object :
                    ApiSubscriberHelper<BaseBean<ShareUserDetailBean.ShareArticles>>(loadService) {
                    override fun onResult(t: BaseBean<ShareUserDetailBean.ShareArticles>) {
                        if (t.errorCode == 0) {
                            currentPage++
                            t.data?.let { articles ->
                                val bean = ShareUserDetailBean(
                                    ShareUserDetailBean.CoinInfo(
                                        0,
                                        0,
                                        "未知的",
                                        0,
                                        articles.datas[0].author
                                    ),
                                    articles
                                ).takeIf { articles.datas.isNotEmpty() }
                                uc.loadCompleteEvent.postValue(bean)
                            }
                        } else {
                            uc.loadCompleteEvent.postValue(null)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                        uc.loadCompleteEvent.postValue(null)
                    }

                })
            return
        }
        userId?.let {
            model.getShareUserDetail(it, currentPage + 1)
                .compose(RxThreadHelper.rxSchedulerHelper(this))
                .subscribe(object :
                    ApiSubscriberHelper<BaseBean<ShareUserDetailBean>>(loadService) {
                    override fun onResult(t: BaseBean<ShareUserDetailBean>) {
                        if (t.errorCode == 0) {
                            currentPage++
                            uc.loadCompleteEvent.postValue(t.data)
                        } else {
                            uc.loadCompleteEvent.postValue(null)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                        uc.loadCompleteEvent.postValue(null)
                    }
                })
        }
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