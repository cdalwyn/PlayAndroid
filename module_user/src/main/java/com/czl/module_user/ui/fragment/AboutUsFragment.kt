package com.czl.module_user.ui.fragment

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.czl.lib_base.adapter.ViewPagerFmAdapter
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.extension.loadBlurImageRes
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentAboutUsBinding
import com.czl.module_user.viewmodel.AboutUsViewModel

/**
 * @author Alwyn
 * @Date 2021/1/21
 * @Description
 */
@Route(path = AppConstants.Router.User.F_ABOUT_US)
class AboutUsFragment : BaseFragment<UserFragmentAboutUsBinding, AboutUsViewModel>() {

    override fun initContentView(): Int {
        return R.layout.user_fragment_about_us
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        val layoutParams = binding.llToolbar.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.setMargins(0, BarUtils.getStatusBarHeight(), 0, 0)
        binding.llToolbar.layoutParams = layoutParams
        binding.viewPager2.apply {
            orientation = ViewPager2.ORIENTATION_VERTICAL
            adapter = ViewPagerFmAdapter(
                childFragmentManager,
                lifecycle,
                arrayListOf(FirstAboutFragment.getInstance(),SecAboutFragment.getInstance())
            )
        }
    }

    override fun initViewObservable() {

    }
}