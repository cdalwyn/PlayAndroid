package com.czl.module_main.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.databinding.CommonRecycleviewBinding
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.adapter.HomeArticleAdapter
import com.czl.module_main.viewmodel.HomeArticleVm

/**
 * @author Alwyn
 * @Date 2020/11/27
 * @Description
 */
class HomeArticleFragment : BaseFragment<CommonRecycleviewBinding, HomeArticleVm>() {

    private lateinit var mAdapter: HomeArticleAdapter

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
//        mAdapter = HomeArticleAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutReference(R.layout.main_article_item_skeleton)
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
                viewModel.currentArticlePage,
                data?.over
            )
        })
    }

}