package com.czl.module_square.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.databinding.CommonRecycleviewBinding
import com.czl.module_square.BR
import com.czl.module_square.R
import com.czl.module_square.adapter.SysContentAdapter
import com.czl.module_square.viewmodel.SystemContentVm

/**
 * @author Alwyn
 * @Date 2020/12/5
 * @Description
 */
class SysContentFragment : BaseFragment<CommonRecycleviewBinding, SystemContentVm>() {
    private lateinit var mAdapter: SysContentAdapter
    private var firstLoad = true

    companion object {
        fun getInstance(cid: String) = SysContentFragment().apply {
            arguments = Bundle().apply { putString("cid", cid) }
        }
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


    override fun isImmersionBarEnabled(): Boolean {
        return false
    }


    override fun onResume() {
        super.onResume()
        // 懒加载
        if (firstLoad) {
            viewModel.cid = arguments?.getString("cid")
            if (!TextUtils.isEmpty(viewModel.cid)) {
                binding.smartCommon.autoRefresh()
            }
        }
    }

    override fun initData() {
        initAdapter()
    }

    override fun initViewObservable() {
        viewModel.loadCompletedEvent.observe(this, { data ->
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
        mAdapter = SysContentAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutReference(R.layout.square_article_item_skeleton)
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            showShimmerAdapter()
        }
    }

    override fun reload() {
        super.reload()
        binding.smartCommon.autoRefresh()
    }
}