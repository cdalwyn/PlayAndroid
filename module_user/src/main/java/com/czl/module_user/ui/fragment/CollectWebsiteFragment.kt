package com.czl.module_user.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.data.bean.CollectWebsiteBean
import com.czl.lib_base.databinding.CommonRecycleviewBinding
import com.czl.lib_base.event.LiveBusCenter
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
        viewModel.loadDataCompleteEvent.observe(this, {
            binding.smartCommon.finishRefresh()
            binding.ryCommon.hideShimmerAdapter()
            if (it == null) {
                loadService.showWithConvertor(-1)
                return@observe
            }
            loadService.showWithConvertor(0)
            if (!mAdapter.hasEmptyView()){
                val emptyView = View.inflate(context, R.layout.common_empty_layout, null)
                emptyView.findViewById<ViewGroup>(R.id.ll_empty).setOnClickListener {
                    binding.smartCommon.autoRefresh()
                }
                mAdapter.setEmptyView(emptyView)
            }
            mAdapter.setDiffNewData(it as MutableList<CollectWebsiteBean>?)
        })
        LiveBusCenter.observeRefreshWebListEvent(this) {
            viewModel.getCollectWebsite()
        }
    }

    override fun reload() {
        super.reload()
        viewModel.getCollectWebsite()
    }
}