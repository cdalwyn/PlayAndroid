package com.czl.module_user.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.blankj.utilcode.constant.MemoryConstants
import com.blankj.utilcode.util.*
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.db.WebHistoryEntity
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.util.DayModeUtil
import com.czl.lib_base.util.FileCacheUtils
import com.czl.lib_base.util.RxThreadHelper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * @author Alwyn
 * @Date 2020/12/7
 * @Description
 */
class UserSettingVm(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()
    val followSysUiModeState: ObservableBoolean = ObservableBoolean(false)
    val cacheSize: ObservableField<String> = ObservableField("")
    val historyVisible: ObservableBoolean = ObservableBoolean(false)

    class UiChangeEvent {
        val switchUiModeEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
        val switchSysModeEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    }

    val onFollowSysModeCheckedCommand: BindingCommand<Boolean> = BindingCommand { checked ->
        followSysUiModeState.set(checked)
        uc.switchSysModeEvent.postValue(checked)
    }
    val onSwitchNightModeCheckedCommand: BindingCommand<Boolean> = BindingCommand { checked ->
        // 不随系统模式才发送
        if (!followSysUiModeState.get()) {
            model.saveUiMode(checked)
            uc.switchUiModeEvent.postValue(checked)
        }
    }

    val onSwitchHistoryCommand: BindingCommand<Boolean> = BindingCommand { checked ->
        model.saveReadHistoryState(checked)
        // 是否屏蔽阅读历史
        LiveBusCenter.postSwitchReadHistoryEvent(checked)
    }

    val onCleanCacheClick: BindingCommand<Void> = BindingCommand(BindingAction {
        if (cacheSize.get() == "0.00B") {
            return@BindingAction
        }
        addSubscribe(Flowable.create(FlowableOnSubscribe<Boolean> {
            it.onNext(CleanUtils.cleanInternalCache())
        }, BackpressureStrategy.BUFFER)
            .compose(RxThreadHelper.rxSchedulerHelper())
            .doOnSubscribe { showLoading("清理中") }
            .subscribe {
                Flowable.timer(300, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        dismissLoading()
                        cacheSize.set("0.00B")
                        showNormalToast("清理成功")
                    }
            })
    })

    val onAboutUsClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        // todo 关于我们  点击用户名称跳转用户详情页
    })

    fun setTvCacheSize() {
        addSubscribe(Flowable.create(FlowableOnSubscribe<String> {
            it.onNext(
                ConvertUtils.byte2FitMemorySize(
                    FileUtils.getLength(PathUtils.getInternalAppCachePath()),
                    2
                )
            )
        }, BackpressureStrategy.BUFFER)
            .compose(RxThreadHelper.rxSchedulerHelper())
            .subscribe {
                cacheSize.set(it)
            })
    }
}