package com.czl.module_login.ui.fragment

import android.graphics.Color
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.widget.EditTextMonitor
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.LoginFragmentLoginBinding
import com.czl.module_login.viewmodel.LoginViewModel
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

@Route(path = AppConstants.Router.Login.F_LOGIN)
class LoginFragment : BaseFragment<LoginFragmentLoginBinding, LoginViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(): Int {
        return R.layout.login_fragment_login
    }

    override fun onSupportVisible() {
        ImmersionBar.with(this).statusBarDarkFont(false).init()
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        initBtnState()
    }

    private fun initBtnState() {
        binding.btnLogin.isEnabled = false
        val accountSubject = PublishSubject.create<String>()
        val pwdSubject = PublishSubject.create<String>()
        binding.etAccount.addTextChangedListener(EditTextMonitor(accountSubject))
        binding.etPwd.addTextChangedListener(EditTextMonitor(pwdSubject))
        viewModel.addSubscribe(Observable.combineLatest(
            accountSubject,
            pwdSubject,
            { account: String, pwd: String ->
                account.isNotBlank() && pwd.isNotBlank()
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.btnLogin.isEnabled = it
                binding.btnLogin.setTextColor(
                    if (it) Color.parseColor("#000000") else Color.parseColor("#ffffff")
                )
                binding.btnLogin.setBackgroundResource(if (it) R.drawable.shape_round_white else R.drawable.gray_btn_corner_10dp)
            })
    }

    override fun initViewObservable() {
        LiveBusCenter.observeRegisterSuccessEvent(this) {
            viewModel.account.set(it.account)
            viewModel.pwd.set(it.pwd)
        }
    }


}