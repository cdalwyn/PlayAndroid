package com.czl.module_login.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_login.R


/**
 * @author Alwyn
 * @Date 2020/10/15
 * @Description
 */
class RegisterViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {

    val tvAccount: ObservableField<String> = ObservableField("")
    val tvPwd: ObservableField<String> = ObservableField("")
    val tvRePwd: ObservableField<String> = ObservableField("")

    val onAccountChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        tvAccount.set(it)
    })

    val onPwdChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        tvPwd.set(it)
    })

    val onRePwdChangeCommand: BindingCommand<String> = BindingCommand(BindingConsumer {
        tvRePwd.set(it)
    })

    val onBackClick:View.OnClickListener = View.OnClickListener {
        finish()
    }

    // 注册
    val onRegisterClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        model.register(tvAccount.get()!!, tvPwd.get()!!, tvRePwd.get()!!)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .doOnSubscribe { showLoading() }
            .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                override fun onResult(t: BaseBean<*>) {
                    dismissLoading()
                    if (t.errorCode == 0) {
                        showSuccessToast(application.getString(R.string.login_register_success))
                        LiveBusCenter.postRegisterSuccessEvent(tvAccount.get(), tvPwd.get())
                        finish()
                    }
                }

                override fun onFailed(msg: String?) {
                    dismissLoading()
                    showErrorToast(msg)
                }

            })
    })

    // 前往登录
    val onBackToLoginCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        finish()
    })

}