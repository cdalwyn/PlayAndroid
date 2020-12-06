package com.czl.module_user.ui.fragment

import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.adapter.ViewPagerFmAdapter
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentCollectBinding
import com.czl.module_user.viewmodel.UserCollectVm
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author Alwyn
 * @Date 2020/11/18
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_COLLECT)
class UserCollectFragment : BaseFragment<UserFragmentCollectBinding, UserCollectVm>() {
    override fun initContentView(): Int {
        return R.layout.user_fragment_collect
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun enableLazy(): Boolean {
        return false
    }

    override fun initData() {
        viewModel.tvTitle.set("我的收藏")
        initViewPagerFragment()
    }

    private fun initViewPagerFragment() {
        val fragments =
            arrayListOf(CollectArticleFragment.getInstance(), CollectWebsiteFragment.getInstance())
        binding.viewpager.apply {
            adapter = ViewPagerFmAdapter(childFragmentManager, lifecycle, fragments)
        }
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            if (position == 0) {
                tab.text = "文章"
            } else {
                tab.text = "网站"
            }
        }.attach()
        if (!TextUtils.isEmpty(arguments?.getString(AppConstants.BundleKey.WEB_MENU_KEY))){
            binding.viewpager.currentItem = 1
        }
    }
}