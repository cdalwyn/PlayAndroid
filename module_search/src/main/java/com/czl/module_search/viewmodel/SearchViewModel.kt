package com.czl.module_search.viewmodel

import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.SearchDataBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_search.BR
import com.czl.module_search.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class SearchViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    private var currentPage = 0
    var keyword = ""

    val uc = UiChangeEvent()

    inner class UiChangeEvent {
        val searchCancelEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val finishLoadEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
        val moveTopEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    /*左边返回按钮的显示*/
    var backVisibility: ObservableField<Boolean> = ObservableField(true)

    /*右边取消按钮的显示*/
    var cancelVisibility: ObservableField<Boolean> = ObservableField(false)

    /*搜索框占位内容*/
    var searchPlaceHolder: ObservableField<String> = ObservableField("")

    /*置顶*/
    val fabOnClickListener: View.OnClickListener = View.OnClickListener {
        uc.moveTopEvent.call()
    }

    val searchItemBinding: ItemBinding<SearchItemViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.search_item)
    val searchItemList: ObservableList<SearchItemViewModel> = ObservableArrayList()

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        getSearchDataByKeyword(keyword)
    })

    val onLoadMoreCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage += 1
        getSearchDataByKeyword(keyword, currentPage)
    })

    val onSearchLeftCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        finish()
    })

    val onSearchStateCommand: BindingCommand<Boolean> = BindingCommand(BindingConsumer { focused ->
        cancelVisibility.set(focused)
        backVisibility.set(!focused)
    })

    val onSearchCancelClick: View.OnClickListener = View.OnClickListener {
        uc.searchCancelEvent.call()
    }

    val onSearchConfirmCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        if (keyword.isBlank()) {
            showNormalToast("搜索内容不能为空喔~")
            return@BindingConsumer
        }
        addSubscribe(model.saveUserSearchHistory(it)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { saved ->
                if (saved) LiveBusCenter.postSearchHistoryEvent()
            })
        searchPlaceHolder.set(it)
        keyword = it
        getSearchDataByKeyword(it)

    })

    fun getSearchDataByKeyword(key: String, page: Int = 0) {
        model.searchByKeyword(page.toString(), key)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<SearchDataBean>>() {
                override fun onResult(t: BaseBean<SearchDataBean>) {
                    uc.finishLoadEvent.postValue(t.data?.over)
                    if (0 == t.errorCode) {
                        t.data?.let {
                            if (page == 0) {
                                searchItemList.clear()
                            }
                            for (data in it.datas) {
                                searchItemList.add(SearchItemViewModel(this@SearchViewModel, data))
                            }
                        }
                    } else {
                        if (currentPage > 0) currentPage -= 1
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.finishLoadEvent.postValue(false)
                    showErrorToast(msg)
                    if (currentPage > 0) currentPage -= 1
                }
            })
    }


}