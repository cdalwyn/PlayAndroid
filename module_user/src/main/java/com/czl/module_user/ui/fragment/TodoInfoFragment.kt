package com.czl.module_user.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.TodoBean
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentTodoInfoBinding
import com.czl.module_user.viewmodel.TodoInfoFmViewModel


/**
 * @author Alwyn
 * @Date 2021/1/12
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_TODO_INFO)
class TodoInfoFragment:BaseFragment<UserFragmentTodoInfoBinding,TodoInfoFmViewModel>() {
    override fun initContentView(): Int {
        return R.layout.user_fragment_todo_info
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("待办编辑")
        binding.data = arguments?.getParcelable(AppConstants.BundleKey.TODO_INFO_DATA)
    }

    override fun initViewObservable() {

    }
}