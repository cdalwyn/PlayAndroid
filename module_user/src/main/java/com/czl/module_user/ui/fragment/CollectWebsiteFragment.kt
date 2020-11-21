package com.czl.module_user.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.data.bean.CollectWebsiteBean
import com.czl.lib_base.databinding.CommonRecycleviewBinding
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserCollectWebAdapter
import com.czl.module_user.viewmodel.CollectWebsiteVm

/**
 * @author Alwyn
 * @Date 2020/11/18
 * @Description
 */
class CollectWebsiteFragment : BaseFragment<CommonRecycleviewBinding, CollectWebsiteVm>() {

    companion object {
        fun getInstance(): CollectWebsiteFragment = CollectWebsiteFragment()
    }

    override fun initContentView(): Int {
        return R.layout.common_recycleview
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun enableSwipeBack(): Boolean {
        return false
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {

        binding.smartCommon.setEnableLoadMore(false)
        binding.smartCommon.autoRefresh()

    }

    override fun initViewObservable() {
        val mAdapter = UserCollectWebAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutReference(R.layout.user_item_collect_skeleton)
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            showShimmerAdapter()
        }
        viewModel.uC.getScrollTopEvent().observe(this, {
            binding.ryCommon.smoothScrollToPosition(0)
        })
        viewModel.loadDataCompleteEvent.observe(this, {
            binding.smartCommon.finishRefresh()
            binding.ryCommon.hideShimmerAdapter()
            mAdapter.setDiffNewData(it as MutableList<CollectWebsiteBean>?)
        })
    }
}