package com.czl.module_login.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.route.RouteCenter
import com.czl.lib_base.util.DayModeUtil
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.LoginActivitySplashBinding
import com.czl.module_login.viewmodel.SplashViewModel
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import me.jessyan.autosize.internal.CancelAdapt
import java.util.*
import java.util.concurrent.TimeUnit

@Route(path = AppConstants.Router.Login.A_SPLASH)
class SplashActivity : BaseActivity<LoginActivitySplashBinding, SplashViewModel>(),CancelAdapt {

    private val arrayLight = arrayListOf(R.drawable.splash_bg_light, R.drawable.bg_splash_light2)
    private val arrayDark = arrayListOf(R.drawable.splash_bg_dark, R.drawable.bg_splash_dark2)

    override fun initContentView(): Int {
        return R.layout.login_activity_splash
    }

    override fun initParam() {
        window.setBackgroundDrawableResource(R.color.white)
        ImmersionBar.hideStatusBar(window)
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
//        initSplashBg()
        toLogin()
    }

    private fun initSplashBg() {
        Glide.with(this).load(
            if (DayModeUtil.isNightMode(this))
                arrayDark[Random().nextInt(arrayDark.size)]
            else arrayLight[Random().nextInt(arrayLight.size)]
        )
            .override(ScreenUtils.getAppScreenWidth(), ScreenUtils.getAppScreenHeight())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .skipMemoryCache(true)
            .into(binding.ivSplash)
    }

    private fun toLogin() {
        viewModel.addSubscribe(
            Flowable.timer(1500L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (viewModel.model.getLoginName().isNullOrBlank()) {
                        startContainerActivity(AppConstants.Router.Login.F_LOGIN)
                        overridePendingTransition(R.anim.h_fragment_enter, 0)
                    } else {
                        RouteCenter.navigate(AppConstants.Router.Main.A_MAIN)
                    }
                    finish()
                })
    }

    override fun useBaseLayout(): Boolean {
        return false
    }
}