package com.czl.module_login.viewmodel

import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.DataRepository
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.mvvm.entity.UserBean
import com.czl.lib_base.route.RouteCenter
import com.czl.lib_base.util.RxThreadHelper
import me.goldze.mvvmhabit.utils.ToastUtils


/**
 * @author Alwyn
 * @Date 2020/10/15
 * @Description
 */
class LoginViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    var account = ObservableField("cdalwyn")
    var pwd = ObservableField("123456")

    var btnLoginClick: BindingCommand<Any> = BindingCommand(BindingAction {
        loginByPwd()
    })

    private fun loginByPwd() {
        if (account.get().isNullOrBlank() || pwd.get().isNullOrBlank()) {
            ToastUtils.showShort("账号或密码不能为空")
            return
        }
        model?.apply {
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