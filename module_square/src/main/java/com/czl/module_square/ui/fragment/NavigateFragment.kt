package com.czl.module_square.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.databinding.CommonRecyclerviewBinding
import com.czl.module_square.BR
import com.czl.module_square.R
import com.czl.module_square.adapter.NavParentAdapter
import com.czl.module_square.viewmodel.NavigateViewModel

/**
 * @author Alwyn
 * @Date 2020/11/29
 * @Description 导航
 */
@Route(path = AppConstants.Router.Square.F_NAV)
class NavigateFragment :BaseFragment<CommonRecyclerviewBinding,NavigateViewModel>(){
    private lateinit var mAdapter:NavParentAdapter
    override fun initContentView(): Int {
        return R.layout.common_recyclerview
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("导航")
        binding.smartCommon.setEnableLoadMore(false)
        mAdapter = NavParentAdapter(this)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
            adapter = mAdapter
        }
        binding.smartCommon.autoRefresh()
    }

    override fun initViewObservable() {
        viewModel.loadCompleteEvent.observe(this) { data ->
            if (data == null) {
                binding.smartCommon.finishRefresh(false)
                return@observe
            }
            ryCommon = binding.ryCommon
            binding.smartCommon.finishRefresh(true)
            if (!mAdapter.hasEmptyView()) {
                val emptyView =
                    View.inflate(context, com.czl.lib_base.R.layout.common_empty_layout, null)
                emptyView.findViewById<ViewGroup>(com.czl.lib_base.R.id.ll_empty)
                    .setOnClickListener {
                        binding.smartCommon.autoRefresh()
                    }
                mAdapter.setEmptyView(emptyView)
            }
            mAdapter.setList(data)
        }
    }

    override fun reload() {
        super.reload()
        binding.smartCommon.autoRefresh()
    }
}