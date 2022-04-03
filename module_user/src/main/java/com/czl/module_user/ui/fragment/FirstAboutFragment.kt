package com.czl.module_user.ui.fragment

import android.graphics.Color
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.BarUtils
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.extension.loadBlurImageRes
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentFirstAboutBinding
import com.czl.module_user.viewmodel.FirstAboutViewModel
import com.gyf.immersionbar.ImmersionBar

/**
 * @author Alwyn
 * @Date 2021/1/21
 * @Description
 */
class FirstAboutFragment : BaseFragment<UserFragmentFirstAboutBinding, FirstAboutViewModel>() {

    companion object {
        fun getInstance(): FirstAboutFragment = FirstAboutFragment()
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

    override fun onSupportVisible() {
        super.onSupportVisible()
        ImmersionBar.with(this).statusBarDarkFont(false).init()
    }

    override fun initData() {
        view?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun initViewObservable() {
        viewModel.showSecPageEvent.observe(this) {
            if (parentFragment is AboutUsFragment) {
                (parentFragment as AboutUsFragment).binding.viewPager2.currentItem = 1
            }
        }
    }
}