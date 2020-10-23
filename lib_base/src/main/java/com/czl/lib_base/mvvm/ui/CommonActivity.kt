package com.czl.lib_base.mvvm.ui

import com.czl.lib_base.BR
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.databinding.CommonContainerBinding
import com.czl.lib_base.mvvm.viewmodel.CommonViewModel

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description Fragment容器 根据BUNDLE_KEY加载根Fragment
 */
class CommonActivity : BaseActivity<CommonContainerBinding, CommonViewModel>() {

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
        when (intent.getIntExtra(BaseViewModel.ParameterField.BUNDLE_KEY, 0)) {
            0 -> {

            }
            1 -> {

            }
        }

    }

}