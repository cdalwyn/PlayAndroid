package com.czl.module_user.viewmodel

import androidx.databinding.ObservableInt
import com.czl.lib_base.annotation.TodoOrder
import com.czl.lib_base.annotation.TodoPriority
import com.czl.lib_base.annotation.TodoType
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.bean.CollectArticleBean
import com.czl.lib_base.data.bean.TodoBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper


/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class UserTodoViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    var currentPage = 1

    @TodoType
    var todoType: Int = TodoType.ALL
    var status = -1

    @TodoPriority
    var priority = 0

    @TodoOrder
    var orderBy = TodoOrder.createDesc
    val uc = UiChangeEvent()

    class UiChangeEvent {
        val refreshCompleteEvent: SingleLiveEvent<TodoBean?> = SingleLiveEvent()
    }

    override fun refreshCommand() {
        currentPage = 0
        getTodoList()
    }

    override fun loadMoreCommand() {
        getTodoList()
    }

    fun getTodoList() {
        model.getTodoList(status, todoType, priority, orderBy, currentPage + 1)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<TodoBean>>(loadService) {
                override fun onResult(t: BaseBean<TodoBean>) {
                    if (t.errorCode == 0) {
                        currentPage++
                        uc.refreshCompleteEvent.postValue(t.data)
                    }else{
                        uc.refreshCompleteEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.refreshCompleteEvent.postValue(null)
                }
            })
    }
}