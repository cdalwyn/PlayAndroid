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
data class SearchHistoryEvent(val code:Int):LiveEvent
data class LogoutEvent(val code:Int):LiveEvent
data class LoginSuccessEvent(val code:Int):LiveEvent
data class RefreshUserFmEvent(val code:Int):LiveEvent
data class RefreshWebListEvent(val code:Int):LiveEvent
data class RefreshCollectStateEvent(val originId:Int):LiveEvent
data class SwitchReadHistoryEvent(val checked: Boolean):LiveEvent
data class TodoListRefreshEvent(val code: Int):LiveEvent
