package com.czl.lib_base.callback

import com.czl.lib_base.R
import com.kingja.loadsir.callback.Callback;

/**
 * @author Alwyn
 * @Date 2020/11/25
 * @Description
 */
class ErrorCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.common_error_layout
    }
}