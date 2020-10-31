package com.czl.lib_base.mvvm.ui

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.czl.lib_base.BR
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.databinding.CommonContainerBinding
import com.czl.lib_base.mvvm.viewmodel.CommonViewModel
import com.czl.lib_base.route.RouteCenter
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description Fragment容器 根据路由地址加载根Fragment
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
        val fragmentPath: String? = intent.getStringExtra(FRAGMENT)
        if (fragmentPath.isNullOrBlank()) {
            return
        }
        val args: Bundle? = intent.getBundleExtra(BUNDLE)
        val fragment: SupportFragment = RouteCenter.navigate(fragmentPath,args) as SupportFragment
        if (findFragment(fragment::class.java) == null) {
            loadRootFragment(R.id.fl_container, fragment)
        }
    }
}