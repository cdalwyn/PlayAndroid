package com.czl.lib_base.data.net

import android.util.Log

object OkLogger {
    private var isLogEnable = true
    private var tag = "retrofit"
    fun debug(isEnable: Boolean) {
        debug(tag, isEnable)
    }

    fun debug(logTag: String, isEnable: Boolean) {
        tag = logTag
        isLogEnable = isEnable
    }

    fun v(msg: String?) {
        v(tag, msg)
    }

    fun v(tag: String?, msg: String?) {
        if (isLogEnable) Log.v(tag, msg)
    }

    fun d(msg: String?) {
        d(tag, msg)
    }

    fun d(tag: String?, msg: String?) {
        if (isLogEnable) Log.d(tag, msg)
    }

    fun i(msg: String?) {
        i(tag, msg)
    }

    fun i(tag: String?, msg: String?) {
        if (isLogEnable) Log.i(tag, msg)
    }

    fun w(msg: String?) {
        w(tag, msg)
    }

    fun w(tag: String?, msg: String?) {
        if (isLogEnable) Log.w(tag, msg)
    }

    fun e(msg: String?) {
        e(tag, msg)
    }

    fun e(tag: String?, msg: String?) {
        if (isLogEnable) Log.e(tag, msg)
    }

    @JvmStatic
    fun printStackTrace(t: Throwable?) {
        if (isLogEnable && t != null) t.printStackTrace()
    }
}