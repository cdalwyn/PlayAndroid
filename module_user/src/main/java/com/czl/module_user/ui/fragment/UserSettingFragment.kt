package com.czl.module_user.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentSettingBinding
import com.czl.module_user.viewmodel.UserSettingVm

/**
 * @author Alwyn
 * @Date 2020/12/7
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_SETTING)
class UserSettingFragment :BaseFragment<UserFragmentSettingBinding,UserSettingVm>() {
    override fun initContentView(): Int {
        return R.layout.user_fragment_setting
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("系统设置")
    }

    override fun initViewObservable() {

    }
}