package com.czl.module_square.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_square.BR
import com.czl.module_square.R
import com.czl.module_square.databinding.SquareFragmentSystemBinding
import com.czl.module_square.viewmodel.SystemTreeVm

/**
 * @author Alwyn
 * @Date 2020/11/29
 * @Description 体系
 */
@Route(path = AppConstants.Router.Square.F_SYSTEM)
class SystemTreeFragment:BaseFragment<SquareFragmentSystemBinding,SystemTreeVm>() {
    override fun initContentView(): Int {
        return R.layout.square_fragment_system
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    override fun initData() {
        viewModel.tvTitle.set("体系")
    }

    override fun initViewObservable() {

    }
}