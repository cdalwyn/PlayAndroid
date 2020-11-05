package com.czl.module_login.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.data.entity.UserBean
import com.czl.lib_base.route.RouteCenter
import com.czl.lib_base.util.RxThreadHelper
import com.czl.lib_base.util.ToastHelper
import me.goldze.mvvmhabit.binding.command.BindingConsumer
import me.goldze.mvvmhabit.utils.ToastUtils
import me.yokeyword.fragmentation.SupportFragment


/**
 * @author Alwyn
 * @Date 2020/10/15
 * @Description
 */
class LoginViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var account = ObservableField("cdalwyn")
    var pwd = ObservableField("123456")

    val onAccountChangeCommand:BindingCommand<String> = BindingCommand(BindingConsumer {
        account.set(it)
    })

    val onPwdChangeCommand:BindingCommand<String> = BindingCommand(BindingConsumer {
        pwd.set(it)
    })

    var btnLoginClick: BindingCommand<Any> = BindingCommand(BindingAction {
        loginByPwd()
    })

    val registerClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        startFragment(RouteCenter.navigate(AppConstants.Router.Login.F_REGISTER) as SupportFragment)
    })

    private fun loginByPwd() {
        if (account.get().isNullOrBlank() || pwd.get().isNullOrBlank()) {
            ToastHelper.showNormalToast("账号或密码不能为空")
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
                            saveLoginName(t.data?.publicName)
                            RouteCenter.navigate(AppConstants.Router.Main.A_MAIN)
                            finish()
                        }
                    }

                    override fun onFailed(msg: String?) {
                        dismissLoading()
                        ToastUtils.showShort(msg)
                    }

                })
        }
    }
}