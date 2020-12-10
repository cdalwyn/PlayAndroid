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

}