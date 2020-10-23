package com.czl.module_login.ui.activity


import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.LoginActivitySplashBinding
import com.czl.lib_base.base.BaseActivity
import com.czl.module_login.viewmodel.SplashViewModel
import com.gyf.immersionbar.ImmersionBar

class SplashActivity : BaseActivity<LoginActivitySplashBinding, SplashViewModel>() {
    override fun initContentView(): Int {
        return R.layout.login_activity_splash
    }

    override fun initParam() {
        ImmersionBar.hideStatusBar(window)
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.goToMain()
    }

    override fun useBaseLayout(): Boolean {
        return false
    }
}