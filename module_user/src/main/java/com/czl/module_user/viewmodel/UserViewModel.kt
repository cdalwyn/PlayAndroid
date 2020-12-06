package com.czl.module_user.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.CollectArticleBean
import com.czl.lib_base.data.bean.UserShareBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.PopDialogUtils
import com.czl.lib_base.util.RxThreadHelper

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class UserViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var tvScore = ObservableField("0")
    var tvCollect = ObservableField("0")
    var tvShare = ObservableField("0")
    val uc = UiChangeEvent()

    class UiChangeEvent {
        val showLoginPopEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val refreshEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val confirmLogoutEvent:SingleLiveEvent<Void> = SingleLiveEvent()
    }

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        getUserCollectData()
        getUserShareData()
        uc.refreshEvent.call()
    })

    val logoutClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.confirmLogoutEvent.call()
    })

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

    val userNameOnClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        if (model.getLoginName().isNullOrBlank()) {
            uc.showLoginPopEvent.call()
        }
    })

    val btnScoreClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.User.F_USER_SCORE)
    })

    val btnCollectClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.User.F_USER_COLLECT)
    })

    val btnShareClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.User.F_USER_SHARE)
    })

    fun getUserShareData() {
        model.getUserShareData()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<UserShareBean>>() {
                override fun onResult(t: BaseBean<UserShareBean>) {
                    if (t.errorCode == 0) {
                        t.data?.let { data ->
                            tvScore.set(data.coinInfo.coinCount.toString())
                            tvShare.set(data.shareArticles.datas.size.toString())
                        }
                    }
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    fun getUserCollectData() {
        model.getCollectArticle()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<CollectArticleBean>>() {
                override fun onResult(t: BaseBean<CollectArticleBean>) {
                    if (t.errorCode == 0) {
                        t.data?.let { data ->
                            tvCollect.set(data.datas.size.toString())
                        }
                    }
                }

                override fun onFailed(msg: String?) {
                }

            })
    }
}