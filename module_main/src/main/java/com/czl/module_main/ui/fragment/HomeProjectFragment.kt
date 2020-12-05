package com.czl.module_main.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.databinding.CommonRecycleviewBinding
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.adapter.HomeProjectAdapter
import com.czl.module_main.viewmodel.HomeProjectVm

/**
 * @author Alwyn
 * @Date 2020/11/27
 * @Description
 */
class HomeProjectFragment : BaseFragment<CommonRecycleviewBinding, HomeProjectVm>() {
    private lateinit var mAdapter: HomeProjectAdapter
    override fun initContentView(): Int {
        return R.layout.common_recycleview
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

    override fun initData() {
        binding.smartCommon.autoRefresh()
    }

    override fun initViewObservable() {
//        mAdapter = HomeProjectAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutReference(R.layout.main_item_project_skeleton)
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            showShimmerAdapter()
        }
        viewModel.loadCompleteEvent.observe(this, { data ->
            handleRecyclerviewData(
                data == null,
                data?.datas as MutableList<*>?,
                mAdapter,
                binding.ryCommon,
                binding.smartCommon,
                viewModel.currentProjectPage,
                data?.over
            )
        })
    }
}