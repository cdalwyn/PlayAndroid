package com.czl.lib_base.widget

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.blankj.utilcode.util.BarUtils
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
import com.lxj.xpopup.core.BottomPopupView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

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

    val tvTitleObservable: ObservableField<String> = ObservableField("登录")

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
        activity.loginPopMap[0] = this
    }

    override fun beforeDismiss() {
        super.beforeDismiss()
        activity.loginPopMap.clear()
    }

    // 切换注册UI
    val onRegisterClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        tvTitleObservable.set("注册")
        registerFlag.set(1)
    })

    // 返回登录UI
    val toLoginClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        tvTitleObservable.set("登录")
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
            val layoutParams = tvLogin.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.setMargins(
                layoutParams.leftMargin,
                BarUtils.getStatusBarHeight() + BarUtils.getActionBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin
            )
            val params = ivBack.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(
                params.leftMargin,
                BarUtils.getStatusBarHeight(),
                params.rightMargin,
                params.bottomMargin
            )
            ivBack.layoutParams = params
            tvLogin.layoutParams = layoutParams
            val accountSub = PublishSubject.create<String>()
            val pwdSub = PublishSubject.create<String>()
            btnLogin.isEnabled = false
            etAccount.addTextChangedListener(EditTextMonitor(accountSub))
            etPwd.addTextChangedListener(EditTextMonitor(pwdSub))
            activity.viewModel.addSubscribe(Observable.combineLatest(accountSub, pwdSub,
                { account: String, pwd: String -> account.isNotBlank() && pwd.isNotBlank() })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    btnLogin.isEnabled = it
                    btnLogin.setBackgroundResource(if (it) R.drawable.shape_round_white else R.drawable.gray_btn_corner_10dp)
                    btnLogin.setTextColor(
                        if (it) Color.parseColor("#000000") else Color.parseColor(
                            "#ffffff"
                        )
                    )
                }
            )
            btnReg.isEnabled = false
            val regAccountSub = PublishSubject.create<String>()
            val regPwdSub = PublishSubject.create<String>()
            val regConfirmPwdSub = PublishSubject.create<String>()
            etRegAccount.addTextChangedListener(EditTextMonitor(regAccountSub))
            etRegPwd.addTextChangedListener(EditTextMonitor(regPwdSub))
            etRegRePwd.addTextChangedListener(EditTextMonitor(regConfirmPwdSub))
            activity.viewModel.addSubscribe(
                Observable.combineLatest(
                    regAccountSub,
                    regPwdSub,
                    regConfirmPwdSub,
                    { account: String, pwd: String, rePwd: String ->
                        account.isNotBlank() && pwd.isNotBlank() && rePwd.isNotBlank()
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        btnReg.apply {
                            isEnabled = it
                            setBackgroundResource(if (it) R.drawable.shape_round_white else R.drawable.gray_btn_corner_10dp)
                            setTextColor(if (it) Color.parseColor("#000000") else Color.parseColor("#ffffff"))
                        }
                    }
            )
            executePendingBindings()
        }
    }

    override fun onDestroy() {
        dataBinding?.unbind()
        super.onDestroy()
    }
}