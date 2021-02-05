package com.czl.module_project.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.adapter.ViewPagerFmAdapter
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.ProjectSortBean
import com.czl.module_project.BR
import com.czl.module_project.R
import com.czl.module_project.databinding.ProjectFragmentProjectBinding
import com.czl.module_project.viewmodel.ProjectViewModel
import com.google.android.material.tabs.TabLayoutMediator


@Route(path = AppConstants.Router.Project.F_PROJECT)
class ProjectFragment : BaseFragment<ProjectFragmentProjectBinding, ProjectViewModel>() {

    override fun initContentView(): Int {
        return R.layout.project_fragment_project
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        val cacheSort = viewModel.getCacheSort()
        if (cacheSort.isNotEmpty()) {
            initViewpager(cacheSort)
        } else {
            viewModel.getProjectSort()
        }
    }

    override fun initViewObservable() {
        viewModel.loadCompleteEvent.observe(this, {
            if (!it.isNullOrEmpty()) {
                viewModel.model.saveCacheListData(it)
            }
            initViewpager(it)
        })
    }

    private fun initViewpager(it: List<ProjectSortBean>) {
        val fragments = arrayListOf<ContentFragment>()
        val tabTitles = arrayListOf<String>()
        for (data in it) {
            binding.tabLayout.addTab(binding.tabLayout.newTab())
            tabTitles.add(data.name)
            fragments.add(ContentFragment.getInstance(data.id.toString()))
        }
        binding.viewpager.apply {
            adapter = ViewPagerFmAdapter(childFragmentManager, lifecycle, fragments)
            // 优化体验设置该属性后第一次将自动加载所有fragment 在子fragment内部添加懒加载机制
            offscreenPageLimit = fragments.size
        }
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun reload() {
        super.reload()
        viewModel.getProjectSort()
    }

}