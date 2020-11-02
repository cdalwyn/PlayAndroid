package com.czl.module_user.ui.fragment

import com.czl.lib_base.base.BaseFragment
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentSecondBinding
import com.czl.module_user.viewmodel.SecFmViewModel
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand
import me.goldze.mvvmhabit.utils.ToastUtils

class SecondFragment : BaseFragment<UserFragmentSecondBinding, SecFmViewModel>() {
    companion object {
        fun newInstance() = SecondFragment()
    }

    override fun initContentView(): Int {
        return R.layout.user_fragment_second
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("SecondFragment")
        viewModel.ivToolbarIconRes.set(R.drawable.ic_setting_more)
        viewModel.ivToolbarIconOnClick = BindingCommand(BindingAction {
            ToastUtils.showShort("这是 SecondFragment 标题栏图标点击事件")
        })
    }
}