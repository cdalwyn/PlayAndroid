package com.czl.lib_base.util

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.blankj.utilcode.util.Utils
import es.dmoral.toasty.Toasty

/**
 * @author Alwyn
 * @Date 2020/3/18
 * @Description
 */
object ToastHelper {
    fun showWarnToast(msg: String?) {
        if (!TextUtils.isEmpty(msg))
            runOnUiThread {
                Toasty.warning(Utils.getApp(), msg!!).show()
            }
    }

    fun showInfoToast(msg: String?) {
        if (!TextUtils.isEmpty(msg))
            runOnUiThread { Toasty.info(Utils.getApp(), msg!!).show() }
    }
    fun showLongInfoToast(msg: String?) {
        if (!TextUtils.isEmpty(msg))
            runOnUiThread { Toasty.info(Utils.getApp(), msg!!,Toast.LENGTH_LONG).show() }
    }

    fun showNormalToast(msg: String?) {
        if (!TextUtils.isEmpty(msg))
            runOnUiThread {
                Toasty.normal(Utils.getApp(), msg!!).show()
            }
    }

    fun showErrorToast(msg: String?) {
        if (!TextUtils.isEmpty(msg))
            runOnUiThread { Toasty.error(Utils.getApp(), msg!!).show() }
    }

    fun showErrorLongToast(msg: String?) {
        if (!TextUtils.isEmpty(msg))
            runOnUiThread { Toasty.error(Utils.getApp(), msg!!, Toast.LENGTH_LONG).show() }
    }

    fun showSuccessToast(msg: String?) {
        if (!TextUtils.isEmpty(msg))
            runOnUiThread {
                if (!TextUtils.isEmpty(msg))
                    Toasty.success(Utils.getApp(), msg!!).show()
            }
    }

    fun showSuccessLongToast(msg: String?) {
        if (!TextUtils.isEmpty(msg))
            runOnUiThread {
                if (!TextUtils.isEmpty(msg))
                    Toasty.success(Utils.getApp(), msg!!,Toasty.LENGTH_LONG).show()
            }
    }

    fun showSuccessLongCenterToast(msg: String?){
        if (!TextUtils.isEmpty(msg))
            runOnUiThread {
                if (!TextUtils.isEmpty(msg))
                    Toasty.success(Utils.getApp(), msg!!,Toasty.LENGTH_LONG).apply {
                        setGravity(Gravity.CENTER,0,0)
                        show()
                    }
            }
    }

    fun showWarnToast(msg: Int) {
        if (!TextUtils.isEmpty(Utils.getApp().getString(msg)))
            runOnUiThread {
                Toasty.warning(Utils.getApp(), msg).show()
            }
    }

    fun showInfoToast(msg: Int) {
        if (!TextUtils.isEmpty(Utils.getApp().getString(msg)))
            runOnUiThread {
                Toasty.info(Utils.getApp(), msg).show()
            }
    }

    fun showNormalToast(msg: Int) {
        if (!TextUtils.isEmpty(Utils.getApp().getString(msg)))
            runOnUiThread {
                Toasty.normal(Utils.getApp(), msg).show()
            }
    }

    fun showLongNormalToast(msg: Int) {
        if (!TextUtils.isEmpty(Utils.getApp().getString(msg)))
            runOnUiThread {
                Toasty.normal(Utils.getApp(), msg,Toast.LENGTH_LONG).show()
            }
    }

    fun showErrorToast(msg: Int) {
        if (!TextUtils.isEmpty(Utils.getApp().getString(msg)))
            runOnUiThread {
                Toasty.error(Utils.getApp(), msg).show()
            }
    }

    fun showSuccessToast(msg: Int) {
        if (!TextUtils.isEmpty(Utils.getApp().getString(msg)))
            runOnUiThread {
                Toasty.success(Utils.getApp(), msg).show()
            }
    }
}