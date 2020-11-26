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

    override fun initData() {
        binding.smartCommon.autoRefresh()
    }

    override fun initViewObservable() {
        val mAdapter = UserCollectAdapter(this)
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
        viewModel.uc.refreshCompleteEvent.observe(this, { data ->
            handleRecyclerviewData(
                data == null,
                data?.datas as MutableList<*>?,
                mAdapter,
                binding.ryCommon,
                binding.smartCommon,
                viewModel.currentPage,
                data?.over
            )
//            binding.ryCommon.hideShimmerAdapter()
//            if (data == null) {
//                binding.smartCommon.finishRefresh(false)
//                binding.smartCommon.finishLoadMore(false)
//                return@observe
//            }
//            if (viewModel.currentPage == 0) {
//                mAdapter.setDiffNewData(data.datas as MutableList<CollectArticleBean.Data>)
//                if (data.over) binding.smartCommon.finishRefreshWithNoMoreData()
//                else binding.smartCommon.finishRefresh(true)
//                return@observe
//            }
//            if (data.over) binding.smartCommon.finishLoadMoreWithNoMoreData()
//            else binding.smartCommon.finishLoadMore(true)
//            mAdapter.addData(data.datas)
        })
    }

    override fun reload() {
        super.reload()
        viewModel.getUserCollectData()
    }
}