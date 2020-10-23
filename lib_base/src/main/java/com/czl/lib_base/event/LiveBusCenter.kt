package com.czl.lib_base.event

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * @author Alwyn
 * @Date 2020/10/16
 * @Description 事件分发中心管理
 * 可通过Kotlin的命名可选参数特性 一个方法即可判断 发送/接收
 * sticky : post/observeSticky
 */
object LiveBusCenter {

//    fun handleMainEvent(
//        value: String? = null,
//        owner: LifecycleOwner? = null,
//        observer:Observer<MainEvent>?=null
//    ) {
//        if (null == owner && value != null) {
//            LiveEventBus.get(MainEvent::class.java).post(MainEvent(value))
//            return
//        }
//        owner?.let{
//            observer?.apply {
//                LiveEventBus.get(MainEvent::class.java).observe(it, this)
//            }
//        }
//    }

    fun postMainEvent(value: String?) {
        LiveEventBus.get(MainEvent::class.java).post(MainEvent(value))
    }

    fun observeMainEvent(owner: LifecycleOwner, func: (t: MainEvent) -> Unit) {
        LiveEventBus.get(MainEvent::class.java).observe(owner, Observer(func))
    }

    fun postTokenExpiredEvent(value: String?) {
        LiveEventBus.get(TokenExpiredEvent::class.java).post(TokenExpiredEvent(value))
    }

    fun observeTokenExpiredEvent(owner: LifecycleOwner, func: (t: TokenExpiredEvent) -> Unit) {
        LiveEventBus.get(TokenExpiredEvent::class.java).observe(owner, Observer(func))
    }
}