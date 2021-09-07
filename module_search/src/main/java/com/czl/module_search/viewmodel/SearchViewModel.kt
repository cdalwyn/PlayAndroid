package com.czl.module_search.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.SearchDataBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import io.reactivex.Observable

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class SearchViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentPage = 0
    var keyword = ""

    val uc = UiChangeEvent()

    inner class UiChangeEvent {
        val searchCancelEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val finishLoadEvent: SingleLiveEvent<SearchDataBean?> = SingleLiveEvent()
        val searchConfirmEvent: SingleLiveEvent<String> = SingleLiveEvent()
        val searchFocusEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    }

    /*左边返回按钮的显示*/
    var backVisibility: ObservableField<Boolean> = ObservableField(true)

    /*右边取消按钮的显示*/
    var cancelVisibility: ObservableField<Boolean> = ObservableField(false)

    /*搜索框占位内容*/
    var searchPlaceHolder: ObservableField<String> = ObservableField("")

    override fun refreshCommand() {
        currentPage = -1
        getSearchDataByKeyword()
    }

    override fun loadMoreCommand() {
        getSearchDataByKeyword()
    }

    val onSearchLeftCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        finish()
    })

    val onSearchStateCommand: BindingCommand<Boolean> = BindingCommand { focused ->
        cancelVisibility.set(focused)
        backVisibility.set(!focused)
        uc.searchFocusEvent.postValue(focused)
    }

    val onSearchCancelClick: View.OnClickListener = View.OnClickListener {
        uc.searchCancelEvent.call()
    }

    val onSearchConfirmCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        if (keyword.isBlank()) {
            uc.searchCancelEvent.call()
            return@BindingConsumer
        }
        uc.searchConfirmEvent.postValue(it)
    })

    fun getSearchDataByKeyword() {
        model.searchByKeyword((currentPage + 1).toString(), keyword)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<SearchDataBean>>() {
                override fun onResult(t: BaseBean<SearchDataBean>) {
                    if (0 == t.errorCode) {
                        currentPage++
                        uc.finishLoadEvent.postValue(t.data)
                    } else {
                        uc.finishLoadEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.finishLoadEvent.postValue(null)
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

}