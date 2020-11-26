package com.czl.module_user.viewmodel

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.UserScoreBean
import com.czl.lib_base.data.bean.UserScoreDetailBean
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class UserScoreVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadCompleteEvent = SingleLiveEvent<Boolean>()
        val moveTopEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val getTotalScoreEvent: SingleLiveEvent<UserScoreBean?> = SingleLiveEvent()
        val loadDataFinishEvent: SingleLiveEvent<List<UserScoreDetailBean.Data>> = SingleLiveEvent()
    }

//    val itemBinding: ItemBinding<ScoreItemVm> =
//        ItemBinding.of(BR.viewModel, R.layout.user_item_score)
//
//    val scoreList: ObservableList<ScoreItemVm> = ObservableArrayList()

    var currentPage = 1
    val tvTotalScore: ObservableField<String> = ObservableField("0")
    var userScore: String = ""
    var userScoreRank = ""

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = 0
        getTotalScore()
    })

    val fabOnClickListener: View.OnClickListener = View.OnClickListener {
        uc.moveTopEvent.call()
    }

    val onRankClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startFragment(AppConstants.Router.User.F_USER_RANK, Bundle().apply {
            putString(AppConstants.BundleKey.USER_SCORE, userScore)
            putString(AppConstants.BundleKey.USER_RANK, userScoreRank)
        })
    })

    fun getTotalScore() {
        model.getUserScore()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<UserScoreBean>>() {
                override fun onResult(t: BaseBean<UserScoreBean>) {
                    if (t.errorCode == 0) {
                        t.data?.let {
//                            tvTotalScore.set(it.coinCount.toString())
                            userScore = it.coinCount.toString()
                            userScoreRank = it.rank.toString()
                            uc.getTotalScoreEvent.postValue(it)
                            getScoreDetails()
                        }
                    } else {
                        uc.getTotalScoreEvent.postValue(null)
                        uc.loadCompleteEvent.postValue(false)
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.getTotalScoreEvent.postValue(null)
                    showErrorToast(msg)
                    uc.loadCompleteEvent.postValue(false)
                }

            })
    }

    val onLoadMoreCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        getScoreDetails()
    })

    fun getScoreDetails() {
        model.getUserScoreDetail((currentPage + 1).toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<UserScoreDetailBean>>() {
                override fun onResult(t: BaseBean<UserScoreDetailBean>) {
                    uc.loadCompleteEvent.postValue(t.data?.over)
                    if (t.errorCode == 0) {
                        currentPage++
                        t.data?.let {
                            uc.loadDataFinishEvent.postValue(it.datas)
                        }
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadCompleteEvent.postValue(false)
                    showErrorToast(msg)
                }

            })
    }
}