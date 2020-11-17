package com.czl.module_square.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_square.BR
import com.czl.module_square.R
import com.czl.module_square.databinding.SquareFragmentSquareBinding
import com.czl.module_square.viewmodel.SquareViewModel


@Route(path = AppConstants.Router.Square.F_SQUARE)
class SquareFragment : BaseFragment<SquareFragmentSquareBinding, SquareViewModel>() {
    override fun initContentView(): Int {
        return R.layout.square_fragment_square
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun enableSwipeBack(): Boolean {
        return false
    }

}