package com.czl.lib_base.mvvm.viewmodel

import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.bean.CollectWebsiteBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper

/**
 * @author Alwyn
 * @Date 2020/10/31
 * @Description
 */
class WebFmViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val uc = UiChangeEvent()
    val collectFlag = ObservableField(false)
    val canForwardFlag = ObservableField(false)
    val canGoBackFlag = ObservableField(false)

    class UiChangeEvent {
        val closeEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val collectEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        val goForwardEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    }

    override fun setToolbarRightClick() {
        uc.closeEvent.call()
    }

    val onCollectClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        uc.collectEvent.call()
    })
    val onMenuClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        showNormalToast("菜单")
    })

    val onGoForwardClick: View.OnClickListener = View.OnClickListener {
        uc.goForwardEvent.call()
    }

    /**
     * 获取当前用户所有收藏的网站来判断当前url是否已收藏
     * 由于该接口需登录 暂不调用该接口
     */
    fun getCollectWebsite(url: String?) {
        model.getUserCollectWebsite()
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<List<CollectWebsiteBean>>>() {
                override fun onResult(t: BaseBean<List<CollectWebsiteBean>>) {
                    if (t.errorCode == 0) {
                        t.data?.let { list ->
                            if (list.map { it.link }.contains(url)) {
                                collectFlag.set(true)
                            } else {
                                collectFlag.set(false)
                            }
                        }
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                }
            })
    }

    fun collectWebsite(name: String?, link: String?) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(link)) {
            showNormalToast("网站标题或链接不能为空~")
            return
        }
        model.collectWebsite(name!!, link!!)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                override fun onResult(t: BaseBean<Any?>) {
                    if (t.errorCode == 0) {
                        collectFlag.set(true)
                        LiveBusCenter.postRefreshWebListEvent()
                    }
                }

                override fun onFailed(msg: String?) {
                    showErrorToast(msg)
                }
            })
    }

    fun unCollectWebsite(id: String?) {
        if (TextUtils.isEmpty(id)) return
        model.deleteUserCollectWeb(id!!)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any?>>() {
                override fun onResult(t: BaseBean<Any?>) {
                    if (t.errorCode == 0) {
                        collectFlag.set(false)
                        LiveBusCenter.postRefreshWebListEvent()
                    }
                }

                override fun onFailed(msg: String?) {

                }
            })
    }
}