package com.czl.lib_base.widget

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.data.bean.UserBean
import com.czl.lib_base.databinding.PopLoginBinding
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.util.RxThreadHelper
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils

/**
 * @author Alwyn
 * @Date 2020/11/14
 * @Description
 */
@SuppressLint("ViewConstructor")
class LoginPopView(val activity: BaseActivity<*, *>) : BottomPopupView(activity) {

    private var dataBinding: PopLoginBinding? = null

    val registerFlag: ObservableInt = ObservableInt(0)
    val tvLoginAccount = ObservableField("")
    val tvLoginPwd = ObservableField("")

    // 登录
    val onLoginClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        dataBinding?.let {
            activity.dataRepository.userLogin(
                it.etAccount.text.toString().trim(),
                it.etPwd.text.toString().trim()
            )
                .compose(RxThreadHelper.rxSchedulerHelper(activity.viewModel))
                .doOnSubscribe { activity.viewModel.showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<UserBean>>() {
                    override fun onResult(t: BaseBean<UserBean>) {
                        activity.viewModel.dismissLoading()
                        if (t.errorCode == 0) {
                            t.data?.let { data ->
                                activity.dataRepository.saveUserData(data)
                                tvLoginPwd.set("")
                                tvLoginPwd.set("")
                                LiveBusCenter.postLoginSuccessEvent()
                                dismiss()
                            }
                        }
                    }

                    override fun onFailed(msg: String?) {
                        activity.viewModel.dismissLoading()
                        activity.showErrorToast(msg)
                    }

                })
        }
    })

    override fun doAfterShow() {
        super.doAfterShow()
        registerFlag.set(0)
    }
    // 切换注册UI
    val onRegisterClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        registerFlag.set(1)
    })

    // 返回登录UI
    val toLoginClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        registerFlag.set(0)
    })

    // 注册
    val registerClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        dataBinding?.let {
            activity.dataRepository.register(
                it.etRegAccount.text.toString().trim(),
                it.etRegPwd.text.toString().trim(),
                it.etRegRePwd.text.toString().trim()
            )
                .compose(RxThreadHelper.rxSchedulerHelper(activity.viewModel))
                .doOnSubscribe { activity.viewModel.showLoading() }
                .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                    override fun onResult(t: BaseBean<*>) {
                        activity.viewModel.dismissLoading()
                        if (t.errorCode == 0) {
                            activity.showSuccessToast("注册成功")
                            tvLoginAccount.set(it.etRegAccount.text.toString().trim())
                            tvLoginPwd.set(it.etRegPwd.text.toString().trim())
                            it.etRegAccount.setText("")
                            it.etRegPwd.setText("")
                            it.etRegRePwd.setText("")
                            registerFlag.set(0)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        activity.viewModel.dismissLoading()
                        activity.showErrorToast(msg)
                    }
                })
        }
    })

    override fun getImplLayoutId(): Int {
        return R.layout.pop_login
    }

    override fun onCreate() {
        super.onCreate()
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.apply {
            pop = this@LoginPopView
            executePendingBindings()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dataBinding?.unbind()
    }


}