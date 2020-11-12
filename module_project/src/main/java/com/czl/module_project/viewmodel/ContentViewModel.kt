package com.czl.module_project.viewmodel

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
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
import com.czl.module_project.BR
import com.czl.module_project.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author Alwyn
 * @Date 2020/11/11
 * @Description
 */
class ContentViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    var currentPage = 1
    var cid = 0

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val refreshCompleteEvent: SingleLiveEvent<ProjectBean> = SingleLiveEvent()
        val moveTopEvent:SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = 1
        getProjectDataByCid()
    })

    val onLoadMoreCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage += 1
        getProjectDataByCid()
    })

    val moveTopClick = View.OnClickListener {
        uc.moveTopEvent.call()
    }

    fun getProjectDataByCid() {
        model.getProjectByCid(currentPage.toString(), cid.toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<ProjectBean>>() {
                override fun onResult(t: BaseBean<ProjectBean>) {
                    if (t.errorCode != 0 && currentPage > 1) {
                        currentPage -= 1
                    }
                    uc.refreshCompleteEvent.postValue(t.data)

                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                    if (currentPage > 1) {
                        currentPage -= 1
                    }
                    uc.refreshCompleteEvent.postValue(null)
                }

            })
    }
}