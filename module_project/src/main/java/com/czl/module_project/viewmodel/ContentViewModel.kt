package com.czl.module_project.viewmodel

import android.view.View
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper

/**
 * @author Alwyn
 * @Date 2020/11/11
 * @Description
 */
class ContentViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    var currentPage = 1
    var cid:String? = null

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val refreshCompleteEvent: SingleLiveEvent<ProjectBean?> = SingleLiveEvent()
        val moveTopEvent:SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = 0
        getProjectDataByCid()
    })

    val onLoadMoreCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        getProjectDataByCid()
    })

    val moveTopClick = View.OnClickListener {
        uc.moveTopEvent.call()
    }

    private fun getProjectDataByCid() {
        cid?.let {
            model.getProjectByCid((currentPage+1).toString(), it)
                .compose(RxThreadHelper.rxSchedulerHelper(this))
                .subscribe(object : ApiSubscriberHelper<BaseBean<ProjectBean>>() {
                    override fun onResult(t: BaseBean<ProjectBean>) {
                        if (t.errorCode == 0) {
                            currentPage++
                            uc.refreshCompleteEvent.postValue(t.data)
                        }else{
                            uc.refreshCompleteEvent.postValue(null)
                        }


                    }

                    override fun onFailed(msg: String?) {
                        showErrorToast(msg)
                        uc.refreshCompleteEvent.postValue(null)
                    }

                })
        }
    }
}