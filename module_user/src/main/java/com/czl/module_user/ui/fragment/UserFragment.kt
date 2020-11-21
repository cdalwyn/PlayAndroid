package com.czl.module_user.ui.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.BaseActivity
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
import com.gyf.immersionbar.ImmersionBar
import org.koin.core.component.KoinApiExtension

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
        binding.userData?.apply {
            viewModel.getUserCollectData()
            viewModel.getUserShareData()
        }
    }

    override fun initViewObservable() {
        LiveBusCenter.observeLogoutEvent(this) {
            binding.userData = null
            viewModel.apply {
                tvCollect.set("0")
                tvScore.set("0")
                tvShare.set("0")
            }
        }
        LiveBusCenter.observeLoginSuccessEvent(this) {
            binding.userData = viewModel.model.getUserData()
            viewModel.getUserCollectData()
            viewModel.getUserShareData()
        }
        LiveBusCenter.observeRefreshUserFmEvent(this){
            viewModel.getUserCollectData()
        }
        viewModel.uc.showLoginPopEvent.observe(this, {
            (activity as BaseActivity<*, *>).showLoginPop()
        })

    }
}