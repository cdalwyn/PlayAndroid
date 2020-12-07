package com.czl.module_user.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.databinding.CommonRecycleviewBinding
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserBrowseAdapter
import com.czl.module_user.viewmodel.UserBrowseVm

/**
 * @author Alwyn
 * @Date 2020/12/7
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_BROWSE)
class UserBrowseFragment : BaseFragment<CommonRecycleviewBinding, UserBrowseVm>() {
    private lateinit var mAdapter:UserBrowseAdapter
    override fun initContentView(): Int {
        return R.layout.common_recycleview
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("阅读历史")
        initAdapter()
        binding.smartCommon.autoRefresh()
    }

    override fun initViewObservable() {
        viewModel.loadCompleteEvent.observe(this,{
            binding.smartCommon.finishRefreshWithNoMoreData()
            binding.ryCommon.hideShimmerAdapter()
            if (!mAdapter.hasEmptyView()){
                val emptyView = View.inflate(context, com.czl.lib_base.R.layout.common_empty_layout, null)
                emptyView.findViewById<ViewGroup>(com.czl.lib_base.R.id.ll_empty).setOnClickListener {
                    binding.smartCommon.autoRefresh()
                }
                mAdapter.setEmptyView(emptyView)
            }
            mAdapter.setList(it)
        })
    }

    private fun initAdapter() {
        mAdapter = UserBrowseAdapter(this)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
            adapter = mAdapter
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            setDemoLayoutReference(R.layout.user_item_browse_skeleton)
            showShimmerAdapter()
        }

    }
}