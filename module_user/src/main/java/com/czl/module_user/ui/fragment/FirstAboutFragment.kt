package com.czl.module_user.ui.fragment

import com.czl.lib_base.base.BaseFragment
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentFirstAboutBinding
import com.czl.module_user.viewmodel.FirstAboutViewModel

/**
 * @author Alwyn
 * @Date 2021/1/21
 * @Description
 */
class FirstAboutFragment:BaseFragment<UserFragmentFirstAboutBinding,FirstAboutViewModel>() {

    companion object{
        fun getInstance():FirstAboutFragment = FirstAboutFragment()
    }

    override fun initContentView(): Int {
        return R.layout.user_fragment_first_about
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {

    }

    override fun initViewObservable() {

    }
}