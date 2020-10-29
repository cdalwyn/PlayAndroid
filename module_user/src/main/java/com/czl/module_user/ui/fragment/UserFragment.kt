package com.czl.module_user.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.util.PermissionUtil
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentFirstBinding
import com.czl.module_user.databinding.UserFragmentUserBinding
import com.czl.module_user.viewmodel.FirstFmViewModel
import com.czl.module_user.viewmodel.UserViewModel
import com.permissionx.guolindev.callback.RequestCallback
import me.goldze.mvvmhabit.utils.ToastUtils

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER)
class UserFragment : BaseFragment<UserFragmentUserBinding, UserViewModel>() {

    override fun initContentView(): Int {
        return R.layout.user_fragment_user
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {

    }

}