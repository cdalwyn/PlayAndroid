package com.czl.module_user.ui.fragment

import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.databinding.CommonRecycleviewBinding
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.viewmodel.CollectWebsiteVm

/**
 * @author Alwyn
 * @Date 2020/11/18
 * @Description
 */
class CollectWebsiteFragment:BaseFragment<CommonRecycleviewBinding,CollectWebsiteVm>() {

    companion object{
        fun getInstance():CollectWebsiteFragment = CollectWebsiteFragment()
    }

    override fun initContentView(): Int {
        return R.layout.common_recycleview
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun enableSwipeBack(): Boolean {
        return false
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {

    }

    override fun initViewObservable() {

    }
}