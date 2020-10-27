package com.czl.lib_base.util.switchicon

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * 应用运行状态注册器
 */
object RunningStateRegister {

    fun register(application: Application, callback: StateCallback) {
        application.registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks() {
            private var startedActivityCount = 0
            override fun onActivityStarted(activity: Activity) {
                if (startedActivityCount == 0) {
                    callback.onForeground()
                }
                startedActivityCount++
            }

            override fun onActivityStopped(activity: Activity) {
                startedActivityCount--
                if (startedActivityCount == 0) {
                    callback.onBackground()
                }
            }
        })
    }

    /**
     * 状态回调
     */
    interface StateCallback {
        fun onForeground()
        fun onBackground()
    }

    open class SimpleActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }
}