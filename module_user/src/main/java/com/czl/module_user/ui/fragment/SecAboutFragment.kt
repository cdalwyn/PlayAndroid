package com.czl.module_user.ui.fragment

import com.czl.lib_base.base.BaseFragment
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentSecAboutBinding
import com.czl.module_user.viewmodel.SecAboutViewModel

/**
 * @author Alwyn
 * @Date 2021/1/21
 * @Description
 */
class SecAboutFragment:BaseFragment<UserFragmentSecAboutBinding,SecAboutViewModel>() {

    companion object{
        fun getInstance():SecAboutFragment = SecAboutFragment()
    }

    override fun initContentView(): Int {
        return R.layout.user_fragment_sec_about
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