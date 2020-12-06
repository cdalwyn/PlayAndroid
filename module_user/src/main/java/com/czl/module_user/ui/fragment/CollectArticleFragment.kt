package com.czl.module_user.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.databinding.CommonRecycleviewBinding
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserCollectAdapter
import com.czl.module_user.viewmodel.CollectArticleVm

/**
 * @author Alwyn
 * @Date 2020/11/18
 * @Description
 */
class CollectArticleFragment : BaseFragment<CommonRecycleviewBinding, CollectArticleVm>() {

    private var firstLoad = true
    private lateinit var mAdapter: UserCollectAdapter

    companion object {
        fun getInstance(): CollectArticleFragment = CollectArticleFragment()
    }

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

    override fun onResume() {
        super.onResume()
        if (firstLoad) {
            binding.ryCommon.showShimmerAdapter()
            binding.smartCommon.autoRefresh()
        }
    }

    override fun initData() {
        initAdapter()
    }

    override fun initViewObservable() {
        viewModel.uc.refreshCompleteEvent.observe(this, { data ->
            if (data != null) {
                firstLoad = false
            }
            handleRecyclerviewData(
                data == null,
                data?.datas as MutableList<*>?,
                mAdapter,
                binding.ryCommon,
                binding.smartCommon,
                viewModel.currentPage,
                data?.over
            )
        })
    }

    private fun initAdapter() {
        if (!this::mAdapter.isInitialized) mAdapter = UserCollectAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutReference(R.layout.user_item_collect_skeleton)
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            showShimmerAdapter()
        }
    }

    override fun reload() {
        super.reload()
        viewModel.getUserCollectData()
    }
}