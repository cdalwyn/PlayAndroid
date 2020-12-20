package com.czl.module_square.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.event.LiveBusCenter
import com.czl.module_square.BR
import com.czl.module_square.R
import com.czl.module_square.adapter.SquareHomeAdapter
import com.czl.module_square.databinding.SquareFragmentSquareBinding
import com.czl.module_square.viewmodel.SquareViewModel


@Route(path = AppConstants.Router.Square.F_SQUARE)
class SquareFragment : BaseFragment<SquareFragmentSquareBinding, SquareViewModel>() {
    private lateinit var mAdapter: SquareHomeAdapter
    override fun initContentView(): Int {
        return R.layout.square_fragment_square
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        initAdapter()
        binding.smartCommon.autoRefresh()
    }

    private fun initAdapter() {
        mAdapter = SquareHomeAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutReference(R.layout.square_item_home_skeleton)
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            showShimmerAdapter()
        }
    }

    override fun initViewObservable() {
        viewModel.uc.scrollTopEvent.observe(this,{
            binding.ryCommon.smoothScrollToPosition(0)
        })
        viewModel.uc.loadCompleteEvent.observe(this, { data ->
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
        // 接收收藏夹取消收藏事件
        LiveBusCenter.observeCollectStateEvent(this) { event ->
            val list = mAdapter.data.filter { it.id == event.originId }
            if (list.isNotEmpty()) list[0].collect = false
            val filterList = mAdapter.data.filter { it.id==event.originId }
            if (filterList.isNotEmpty()) filterList[0].collect = false
        }
    }

    override fun reload() {
        super.reload()
        binding.smartCommon.autoRefresh()
    }

}