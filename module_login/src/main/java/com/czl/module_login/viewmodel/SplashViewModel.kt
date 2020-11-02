package com.czl.module_login.viewmodel

import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.route.RouteCenter
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * @author Alwyn
 * @Date 2020/10/21
 * @Description
 */
class SplashViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    fun goToMain() {
        addSubscribe(
            Flowable.timer(1500L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (model.getLoginName().isNullOrBlank()) {
                        RouteCenter.navigate(AppConstants.Router.Login.A_LOGIN)
                    } else {
                        RouteCenter.navigate(AppConstants.Router.Main.A_MAIN)
                    }
                    finish()
                })
    }
}