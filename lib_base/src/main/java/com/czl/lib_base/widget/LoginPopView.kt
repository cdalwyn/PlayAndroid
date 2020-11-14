package com.czl.lib_base.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.databinding.DataBindingUtil
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.databinding.PopLoginBinding
import com.lxj.xpopup.core.BottomPopupView

/**
 * @author Alwyn
 * @Date 2020/11/14
 * @Description
 */
@SuppressLint("ViewConstructor")
class LoginPopView(activity:BaseActivity<*,*>) :BottomPopupView(activity) {

    private var dataBinding:PopLoginBinding? = null
    val onLoginClickCommand:BindingCommand<Void> = BindingCommand(BindingAction {
        activity.showNormalToast("登录")
    })
    override fun getImplLayoutId(): Int {
        return R.layout.pop_login
    }

    override fun onCreate() {
        super.onCreate()
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.pop = this

    }

    override fun onDestroy() {
        super.onDestroy()
        dataBinding?.unbind()
    }
}