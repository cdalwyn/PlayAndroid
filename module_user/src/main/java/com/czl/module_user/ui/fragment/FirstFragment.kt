package com.czl.module_user.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.util.PermissionUtil
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentFirstBinding
import com.czl.module_user.viewmodel.FirstFmViewModel
import com.permissionx.guolindev.callback.RequestCallback

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
@Route(path = AppConstants.Router.User.F_FIRST)
class FirstFragment : BaseFragment<UserFragmentFirstBinding, FirstFmViewModel>() {

    override fun initContentView(): Int {
        return R.layout.user_fragment_first
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun isImmersionBarEnabled(): Boolean {
        return false
    }

    override fun initData() {
        showNormalToast(arguments?.getString(AppConstants.BundleKey.MAIN2FIRST))
        viewModel.tvTitle.set("FirstFragment")
        viewModel.ivToolbarIconRes.set(R.drawable.ic_setting)
        PermissionUtil.reqStorage(
            fragment = this,
            callback = RequestCallback { allGranted, grantedList, deniedList ->
            })
    }


}