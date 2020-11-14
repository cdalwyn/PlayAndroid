package com.czl.lib_base.route

import android.os.Bundle
import android.os.Parcelable
import com.alibaba.android.arouter.launcher.ARouter
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.mvvm.ui.ContainerFmActivity
import java.io.Serializable

/**
 * @author Alwyn
 * @Date 2020/10/23
 * @Description
 */
object RouteCenter {
    fun navigate(path: String, bundle: Bundle? = null): Any? {
        val build = ARouter.getInstance().build(path)
        return if (bundle == null) build.navigation() else build.with(bundle).navigation()
    }
}