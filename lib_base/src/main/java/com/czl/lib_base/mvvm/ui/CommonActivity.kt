package com.czl.lib_base.mvvm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.czl.lib_base.BR
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseViewModel
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.databinding.CommonContainerBinding
import com.czl.lib_base.mvvm.viewmodel.CommonViewModel
import com.czl.lib_base.route.RouteCenter
import me.goldze.mvvmhabit.base.ContainerActivity
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description Fragment容器 根据BUNDLE_KEY加载根Fragment
 */
class CommonActivity : BaseActivity<CommonContainerBinding, CommonViewModel>() {
    companion object {
        const val FRAGMENT = "fragment"
        const val BUNDLE = "bundle"
    }

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
        val fragmentName: String? = intent.getStringExtra(FRAGMENT)
        require(!(fragmentName == null || "" == fragmentName)) { "找不到Fragment，请使用全路径名进行反射调用" }
        val fragmentClass = Class.forName(fragmentName)
        val fragment = fragmentClass.newInstance() as SupportFragment
        val args: Bundle? = intent.getBundleExtra(ContainerActivity.BUNDLE)
        if (args != null) {
            fragment.arguments = args
        }
        if (findFragment(fragment::class.java) == null) {
            loadRootFragment(R.id.fl_container, fragment)
        }
    }

}