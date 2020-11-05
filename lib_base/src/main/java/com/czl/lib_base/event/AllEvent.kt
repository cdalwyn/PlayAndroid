package com.czl.lib_base.event

import com.jeremyliao.liveeventbus.core.LiveEvent

/**
 * @author Alwyn
 * @Date 2020/10/15
 * @Description 页面通信事件
 */
data class MainEvent(val msg: String?):LiveEvent
data class TokenExpiredEvent(val msg: String?):LiveEvent
data class RegisterSuccessEvent(val account: String?,val pwd:String?):LiveEvent
