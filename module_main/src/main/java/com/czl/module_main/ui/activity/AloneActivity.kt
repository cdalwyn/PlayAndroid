package com.czl.module_main.ui.activity

import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.databinding.CommonContainerBinding
import com.czl.lib_base.mvvm.viewmodel.CommonViewModel
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.ui.fragment.HomeFragment

class AloneActivity : BaseActivity<CommonContainerBinding, CommonViewModel>() {

    override fun initContentView(): Int {
        return R.layout.common_container
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        if (findFragment(HomeFragment::class.java)==null){
            loadRootFragment(R.id.fl_container,HomeFragment())
        }
    }
}