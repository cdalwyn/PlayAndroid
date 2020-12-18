package com.czl.module_login.viewmodel

import android.os.Bundle
import androidx.databinding.ObservableField
import com.czl.lib_base.base.AppManager
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.data.bean.UserBean
import com.czl.lib_base.route.RouteCenter
import com.czl.lib_base.util.RxThreadHelper
import me.yokeyword.fragmentation.SupportFragment


/**
 * @author Alwyn
 * @Date 2020/10/15
 * @Description
 */
class LoginViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var account = ObservableField("")
    var pwd = ObservableField("")

    val onAccountChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        account.set(it)
    })

    val onPwdChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        pwd.set(it)
    })

    var btnLoginClick: BindingCommand<Any> = BindingCommand(BindingAction {
        loginByPwd()
    })

    val registerClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Login.F_REGISTER)
    })

    val touristClickCommand:BindingCommand<Void> = BindingCommand(BindingAction {
        RouteCenter.navigate(AppConstants.Router.Main.A_MAIN)
        AppManager.instance.finishAllActivity()
    })

    private fun loginByPwd() {
        if (account.get().isNullOrBlank() || pwd.get().isNullOrBlank()) {
            showNormalToast("账号或密码不能为空")
            return
        }
        model.apply {
            userLogin(account.get()!!, pwd.get()!!)
                .compose(RxThreadHelper.rxSchedulerHelper(this@LoginViewModel))
                .doOnSubscribe { showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<UserBean>>() {
                    override fun onResult(t: BaseBean<UserBean>) {
                        dismissLoading()
                        if (t.errorCode == 0) {
                            t.data?.let {
                                saveUserData(it)
                            }
                            RouteCenter.navigate(AppConstants.Router.Main.A_MAIN)
                            AppManager.instance.finishAllActivity()
                        }
                    }

                    override fun onFailed(msg: String?) {
                        dismissLoading()
                        showNormalToast(msg)
                    }

                })
        }
    }
}