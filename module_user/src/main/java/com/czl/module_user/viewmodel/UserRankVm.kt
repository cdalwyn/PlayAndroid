package com.czl.module_user.viewmodel

import android.graphics.BitmapFactory
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.bean.*
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.QRCodeUtil
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_user.BR
import com.czl.module_user.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class UserRankVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var currentPage = 1
    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadDataEvent: SingleLiveEvent<UserRankBean?> = SingleLiveEvent()
    }

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = 1
        getScoreRank()
    })

    private fun getScoreRank() {
        model.getScoreRank(currentPage.toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<UserRankBean>>() {
                override fun onResult(t: BaseBean<UserRankBean>) {
                    if (t.errorCode == 0) {
                        uc.loadDataEvent.postValue(t.data)
                    } else {
                        if (currentPage > 1) currentPage -= 1
                        uc.loadDataEvent.postValue(null)
                    }
                }

                override fun onFailed(msg: String?) {
                    if (currentPage > 1) currentPage -= 1
                    showErrorToast(msg)
                    uc.loadDataEvent.postValue(null)
                }
            })
    }

    val onLoadMoreCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage += 1
        getScoreRank()
    })

}