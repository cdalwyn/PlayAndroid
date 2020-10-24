package com.czl.lib_base.mvvm.ui

import android.os.Bundle
import com.czl.lib_base.BR
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.databinding.CommonContainerBinding
import com.czl.lib_base.mvvm.viewmodel.CommonViewModel
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description Fragment容器 根据canonicalName加载根Fragment
 */
class ContainerFmActivity : BaseActivity<CommonContainerBinding, CommonViewModel>() {
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
        if (fragmentName.isNullOrBlank()){
            return
        }
        val fragmentClass = Class.forName(fragmentName)
        val fragment = fragmentClass.newInstance() as SupportFragment
        val args: Bundle? = intent.getBundleExtra(BUNDLE)
        if (args != null) {
            fragment.arguments = args
        }
        if (findFragment(fragment::class.java) == null) {
            loadRootFragment(R.id.fl_container, fragment)
        }
    }

}