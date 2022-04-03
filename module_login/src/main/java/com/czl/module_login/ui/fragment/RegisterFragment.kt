package com.czl.module_login.ui.fragment

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.BarUtils
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.widget.EditTextMonitor
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.LoginFragmentRegisterBinding
import com.czl.module_login.viewmodel.RegisterViewModel
import com.gyf.immersionbar.ImmersionBar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

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

    override fun onSupportVisible() {
        ImmersionBar.with(this).statusBarDarkFont(false).init()
    }

    override fun initData() {
        initBackBtnMargin()
        initBtnState()
    }

    private fun initBtnState() {
        binding.btnRegister.isEnabled = false
        val accountSubject = PublishSubject.create<String>()
        val pwdSubject = PublishSubject.create<String>()
        val pwdConfirmSubject = PublishSubject.create<String>()
        binding.etAccount.addTextChangedListener(EditTextMonitor(accountSubject))
        binding.etPwd.addTextChangedListener(EditTextMonitor(pwdSubject))
        binding.etPwdConfirm.addTextChangedListener(EditTextMonitor(pwdConfirmSubject))
        viewModel.addSubscribe(
            Observable.combineLatest(accountSubject, pwdSubject, pwdConfirmSubject) { account: String, pwd: String, pwdConfirm: String ->
                account.isNotBlank() && pwd.isNotBlank() && pwdConfirm.isNotBlank()
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.btnRegister.isEnabled = it
                binding.btnRegister.setTextColor(if (it) Color.parseColor("#000000") else Color.parseColor("#ffffff"))
                binding.btnRegister.setBackgroundResource(if (it) R.drawable.shape_round_white else R.drawable.gray_btn_corner_10dp)
            })
    }

    private fun initBackBtnMargin() {
        val layoutParams = binding.ivBack.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            BarUtils.getStatusBarHeight(),
            layoutParams.rightMargin,
            layoutParams.bottomMargin
        )
        binding.ivBack.layoutParams = layoutParams
    }
}