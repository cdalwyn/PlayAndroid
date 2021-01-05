package com.czl.module_user.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentTodoBinding
import com.czl.module_user.viewmodel.UserTodoViewModel


/**
 * @author Alwyn
 * @Date 2021/1/5
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_TODO)
class UserTodoFragment : BaseFragment<UserFragmentTodoBinding, UserTodoViewModel>() {
    override fun initContentView(): Int {
        return R.layout.user_fragment_todo
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("待办清单")
        viewModel.ivToolbarIconRes.set(R.drawable.ic_nav)
    }

    override fun initViewObservable() {

    }
}