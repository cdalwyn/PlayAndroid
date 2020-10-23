package com.czl.lib_base.route

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.czl.lib_base.config.AppConstants

/**
 * @author Alwyn
 * @Date 2020/10/23
 * @Description
 */
object RouteCenter {
    fun navigate(path: String, bundle: Bundle? = null) :Any?{
        bundle?.apply {
            return ARouter.getInstance().build(path).with(this).navigation()
        }
        return ARouter.getInstance().build(path).navigation()
    }
}