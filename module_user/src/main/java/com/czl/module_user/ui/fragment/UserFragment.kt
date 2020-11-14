package com.czl.module_user.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.UserBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.loadCircleImageRes
import com.czl.lib_base.util.SpUtils
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentUserBinding
import com.czl.module_user.viewmodel.UserViewModel
import com.google.gson.reflect.TypeToken

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

    override fun enableSwipeBack(): Boolean {
        return false
    }

    override fun initData() {
        binding.userData = viewModel.model.getUserData()
        binding.ivAvatar.loadCircleImageRes(R.mipmap.ic_launcher)
    }

    override fun initViewObservable() {
        LiveBusCenter.observeLogoutEvent(this){
            binding.userData = null
        }
    }
}