package com.czl.module_user.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.CollectionUtils
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.data.bean.CollectWebsiteBean
import com.czl.lib_base.databinding.CommonRecyclerviewBinding
import com.czl.lib_base.event.LiveBusCenter
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserCollectWebAdapter
import com.czl.module_user.viewmodel.CollectWebsiteVm
import java.util.stream.Collectors

/**
 * @author Alwyn
 * @Date 2020/11/18
 * @Description
 */
class CollectWebsiteFragment : BaseFragment<CommonRecyclerviewBinding, CollectWebsiteVm>() {

    private lateinit var mAdapter: UserCollectWebAdapter

    companion object {
        fun getInstance(): CollectWebsiteFragment = CollectWebsiteFragment()
    }

    override fun initContentView(): Int {
        return R.layout.common_recyclerview
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        initAdapter()
        binding.smartCommon.setEnableLoadMore(false)
        binding.smartCommon.autoRefresh()
    }

    override fun initViewObservable() {
        viewModel.loadDataCompleteEvent.observe(this) {
            binding.smartCommon.finishRefresh()
            binding.ryCommon.hideShimmerAdapter()
            if (it == null) {
                return@observe
            }
            if (!mAdapter.hasEmptyView()) {
                val emptyView = View.inflate(context, R.layout.common_empty_layout, null)
                emptyView.findViewById<ViewGroup>(R.id.ll_empty).setOnClickListener {
                    binding.smartCommon.autoRefresh()
                }
                mAdapter.setEmptyView(emptyView)
            }
            mAdapter.setDiffNewData(it as MutableList<CollectWebsiteBean>?)
        }
        LiveBusCenter.observeRefreshWebListEvent(this) {
            viewModel.getCollectWebsite()
        }
    }

    private fun initAdapter() {
        mAdapter = UserCollectWebAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            ryCommon = this
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutReference(R.layout.user_item_collect_skeleton)
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            showShimmerAdapter()
        }
    }

    override fun reload() {
        super.reload()
        viewModel.getCollectWebsite()
    }
}