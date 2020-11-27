package com.czl.module_main.viewmodel

import android.view.View
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.HomeBannerBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import io.reactivex.Observable


/**
 * @author Alwyn
 * @Date 2020/10/29
 * @Description
 */
class HomeViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()

    inner class UiChangeEvent {
        val bannerCompleteEvent: SingleLiveEvent<List<HomeBannerBean>?> = SingleLiveEvent()
        val drawerOpenEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val searchConfirmEvent: SingleLiveEvent<String> = SingleLiveEvent()
        val searchItemClickEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val searchItemDeleteEvent: SingleLiveEvent<Int> = SingleLiveEvent()
    }

    /*打开抽屉*/
    val onDrawerClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.drawerOpenEvent.call()
    })

    /*确认搜索*/
    val onSearchConfirmCommand: BindingCommand<String> = BindingCommand(BindingConsumer { keyword ->
        if (keyword.isBlank()) {
            showNormalToast("搜索内容不能为空喔~")
            return@BindingConsumer
        }
        // 保存到数据库
        addSubscribe(model.saveUserSearchHistory(keyword).subscribe())
        uc.searchConfirmEvent.postValue(keyword)
    })

    /*搜索的历史记录Item点击事件*/
    val onSearchItemClick: SuggestionsAdapter.OnItemViewClickListener =
        object : SuggestionsAdapter.OnItemViewClickListener {
            override fun OnItemDeleteListener(position: Int, v: View?) {
                uc.searchItemDeleteEvent.postValue(position)
            }

            override fun OnItemClickListener(position: Int, v: View?) {
                uc.searchItemClickEvent.postValue(position)
            }

        }

    /**
     * 退出登录
     */
    fun logout() {
        model.logout()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                override fun onResult(t: BaseBean<Any?>) {
                    if (t.errorCode == 0) {
                        model.clearLoginState()
                        LiveBusCenter.postLogoutEvent()
                    }
                }

                override fun onFailed(msg: String?) {
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

    fun unCollectArticle(id: Int): Observable<BaseBean<Any?>> {
        return model.unCollectArticle(id).compose(RxThreadHelper.rxSchedulerHelper(this))
    }


    fun getBanner() {
        model.getBannerData()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<HomeBannerBean>>>() {
                override fun onResult(t: BaseBean<List<HomeBannerBean>>) {
                    if (t.errorCode == 0) {
                        uc.bannerCompleteEvent.postValue(t.data)
                    } else {
                        uc.bannerCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.bannerCompleteEvent.postValue(null)
                    showErrorToast(msg)
                }
            })
    }


}