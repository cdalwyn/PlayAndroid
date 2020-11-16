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
import com.czl.lib_base.data.bean.CollectArticle
import com.czl.lib_base.data.bean.UserScoreBean
import com.czl.lib_base.data.bean.UserScoreDetailBean
import com.czl.lib_base.data.bean.UserShareBean
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
class UserScoreVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()

    class UiChangeEvent {
        val loadCompleteEvent = SingleLiveEvent<Boolean>()
        val moveTopEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val getTotalScoreEvent: SingleLiveEvent<Int> = SingleLiveEvent()
        val loadDataFinishEvent: SingleLiveEvent<List<UserScoreDetailBean.Data>> = SingleLiveEvent()
    }

//    val itemBinding: ItemBinding<ScoreItemVm> =
//        ItemBinding.of(BR.viewModel, R.layout.user_item_score)
//
//    val scoreList: ObservableList<ScoreItemVm> = ObservableArrayList()

    var currentPage = 1
    val tvTotalScore: ObservableField<String> = ObservableField("0")

    val onRefreshCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage = 1
        getTotalScore()
    })

    val fabOnClickListener: View.OnClickListener = View.OnClickListener {
        uc.moveTopEvent.call()
    }

    val onRankClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        // todo 积分排行榜
        showNormalToast("积分排行榜")
    })

    fun getTotalScore() {
        model.getUserScore()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<UserScoreBean>>() {
                override fun onResult(t: BaseBean<UserScoreBean>) {
                    if (t.errorCode == 0) {
                        t.data?.let {
//                            tvTotalScore.set(it.coinCount.toString())
                            uc.getTotalScoreEvent.postValue(it.coinCount)
                            getScoreDetails()
                        }
                    } else {
                        uc.loadCompleteEvent.postValue(false)
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                    uc.loadCompleteEvent.postValue(false)
                }

            })
    }

    val onLoadMoreCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        currentPage += 1
        getScoreDetails()
    })

    fun getScoreDetails() {
        model.getUserScoreDetail(currentPage.toString())
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<UserScoreDetailBean>>() {
                override fun onResult(t: BaseBean<UserScoreDetailBean>) {
                    uc.loadCompleteEvent.postValue(t.data?.over)
                    if (t.errorCode == 0) {
                        t.data?.let {
                            uc.loadDataFinishEvent.postValue(it.datas)
                        }
                    } else {
                        if (currentPage > 1) currentPage -= 1
                    }
                }

                override fun onFailed(msg: String?) {
                    uc.loadCompleteEvent.postValue(false)
                    if (currentPage > 1) currentPage -= 1
                    showErrorToast(msg)
                }

            })
    }
}