package com.czl.lib_base.util

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.czl.lib_base.R
import com.czl.lib_base.base.AppManager
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.config.AppConstants

/**
 * @author Alwyn
 * @Date 2020/12/14
 * @Description
 */
object DayModeUtil {
    fun isNightMode(context: Context): Boolean {
        val mode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

    fun autoModeBySys() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    fun setNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    fun setLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}