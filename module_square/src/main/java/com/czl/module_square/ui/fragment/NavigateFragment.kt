package com.czl.module_square.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_square.BR
import com.czl.module_square.R
import com.czl.module_square.databinding.SquareFragmentNavBinding
import com.czl.module_square.viewmodel.NavigateViewModel

/**
 * @author Alwyn
 * @Date 2020/11/29
 * @Description 导航
 */
@Route(path = AppConstants.Router.Square.F_NAV)
class NavigateFragment :BaseFragment<SquareFragmentNavBinding,NavigateViewModel>(){
    override fun initContentView(): Int {
        return R.layout.square_fragment_nav
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("导航")
    }

    override fun initViewObservable() {

    }
}