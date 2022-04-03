package com.czl.module_square.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.databinding.CommonRecyclerviewBinding
import com.czl.module_square.BR
import com.czl.module_square.R
import com.czl.module_square.adapter.SysContentAdapter
import com.czl.module_square.viewmodel.SystemContentVm

/**
 * @author Alwyn
 * @Date 2020/12/5
 * @Description
 */
@Route(path = AppConstants.Router.Square.F_SYS_CONTENT)
class SysContentFragment : BaseFragment<CommonRecyclerviewBinding, SystemContentVm>() {
    private lateinit var mAdapter: SysContentAdapter
    private var title: String? = null
    private var firstLoad = true

    companion object {
        fun getInstance(cid: String) = SysContentFragment().apply {
            arguments = Bundle().apply {
                putString("cid", cid)
            }
        }
    }

    override fun initParam() {
        title = arguments?.getString(AppConstants.BundleKey.SYS_CONTENT_TITLE)
    }

    override fun initContentView(): Int {
        return R.layout.common_recyclerview
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return !title.isNullOrEmpty()
    }

    override fun isImmersionBarEnabled(): Boolean {
        return !title.isNullOrEmpty()
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
        if (useBaseLayout()) {
            viewModel.tvTitle.set(title)
        }
        initAdapter()
    }

    override fun initViewObservable() {
        viewModel.loadCompletedEvent.observe(this) { data ->
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
        }
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