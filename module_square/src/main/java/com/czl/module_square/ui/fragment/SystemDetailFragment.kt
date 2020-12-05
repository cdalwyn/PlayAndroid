package com.czl.module_square.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.adapter.ViewPagerFmAdapter
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.pojo.GroupItem
import com.czl.module_square.BR
import com.czl.module_square.R
import com.czl.module_square.databinding.SquareFragmentSysDetailBinding
import com.czl.module_square.viewmodel.SystemDetailVm
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author Alwyn
 * @Date 2020/12/5
 * @Description
 */
@Route(path = AppConstants.Router.Square.F_SYS_DETAIL)
class SystemDetailFragment : BaseFragment<SquareFragmentSysDetailBinding, SystemDetailVm>() {
    override fun initContentView(): Int {
        return R.layout.square_fragment_sys_detail
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun isThemeRedStatusBar(): Boolean {
        return true
    }

    override fun initData() {
        val groupItem =
            arguments?.getSerializable(AppConstants.BundleKey.SYSTEM_DETAIL) as GroupItem?
        val position = arguments?.getInt(AppConstants.BundleKey.SYSTEM_DETAIL_POSITION,0)
        groupItem?.let {
            viewModel.tvToolbarTitle.set(it.group)
            val fragments = ArrayList<SysContentFragment>(it.list.size)
            val tabTitles = arrayListOf<String>()
            for (itemInfo in it.list) {
                fragments.add(SysContentFragment.getInstance(itemInfo.cid.toString()))
                tabTitles.add(itemInfo.title)
                binding.tabLayout.addTab(binding.tabLayout.newTab())
            }
            binding.viewpager.apply {
                adapter = ViewPagerFmAdapter(childFragmentManager, lifecycle, fragments)
                // 设置该属性后第一次将自动加载所有fragment
                offscreenPageLimit = fragments.size
            }
            TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
            if (position != null) {
                binding.viewpager.currentItem = position
            }
        }
    }

    override fun initViewObservable() {

    }
}