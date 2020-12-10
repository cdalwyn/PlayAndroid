package com.czl.module_login.ui.activity


import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.LoginActivitySplashBinding
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.route.RouteCenter
import com.czl.module_login.viewmodel.SplashViewModel
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

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
        viewModel.addSubscribe(
            Flowable.timer(1500L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (viewModel.model.getLoginName().isNullOrBlank()){
                        startContainerActivity(AppConstants.Router.Login.F_LOGIN)
                        overridePendingTransition(R.anim.h_fragment_enter,0)
                    }else{
                        RouteCenter.navigate(AppConstants.Router.Main.A_MAIN)
                    }
                    finish()
                })
    }

    override fun useBaseLayout(): Boolean {
        return false
    }
}