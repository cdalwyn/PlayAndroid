package com.czl.module_login.ui.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.LoginFragmentRegisterBinding
import com.czl.module_login.viewmodel.RegisterViewModel

@Route(path = AppConstants.Router.Login.F_REGISTER)
class RegisterFragment : BaseFragment<LoginFragmentRegisterBinding, RegisterViewModel>() {
    override fun initContentView(): Int {
        return R.layout.login_fragment_register
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }
}