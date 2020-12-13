package com.czl.module_square.viewmodel

import android.view.View
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.SquareListBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import io.reactivex.Observable

/**
 * @author Alwyn
 * @Date 2020/10/29
 * @Description
 */
class SquareViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    var currentPage = 0
    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadCompleteEvent: SingleLiveEvent<SquareListBean?> = SingleLiveEvent()
        val scrollTopEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val onLoadMoreListener: BindingCommand<Void> = BindingCommand(BindingAction {
        getSquareList()

    })
    val onRefreshListener: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = -1
        getSquareList()
    })

    val fabOnClickListener: View.OnClickListener = View.OnClickListener {
        uc.scrollTopEvent.call()
    }

    val onSystemTreeClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Square.F_SYSTEM)
    })

    val onNavClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Square.F_NAV)
    })

    val onEditShareClick: BindingCommand<Void> = BindingCommand(BindingAction {
        uC.showSharePopEvent.call()
    })

    /**
     * 收藏
     */
    fun collectArticle(id: Int): Observable<BaseBean<Any?>> {
        return model.collectArticle(id).compose(RxThreadHelper.rxSchedulerHelper(this))
    }

    fun unCollectArticle(id: Int): Observable<BaseBean<Any?>> {
        return model.unCollectArticle(id).compose(RxThreadHelper.rxSchedulerHelper(this))
    }

    private fun getSquareList() {
        model.getSquareList(currentPage + 1)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<SquareListBean>>() {
                override fun onResult(t: BaseBean<SquareListBean>) {
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