package com.czl.module_login.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_login.databinding.LoginFragmentLoginBinding
import com.czl.module_login.databinding.LoginFragmentLoginBindingImpl
import com.czl.module_login.viewmodel.LoginViewModel
import com.gyf.immersionbar.ImmersionBar

@Route(path = AppConstants.Router.Login.F_LOGIN)
class LoginFragment : BaseFragment<LoginFragmentLoginBinding, LoginViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(): Int {
        return R.layout.login_fragment_login
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        ImmersionBar.with(this).statusBarDarkFont(false).init()
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

}