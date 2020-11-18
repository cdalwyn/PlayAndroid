package com.czl.module_user.ui.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.UserRankBean
import com.czl.lib_base.data.bean.UserScoreBean
import com.czl.lib_base.extension.loadCircleImageRes
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserRankAdapter
import com.czl.module_user.databinding.UserFragmentRankBinding
import com.czl.module_user.viewmodel.UserRankVm

@Route(path = AppConstants.Router.User.F_USER_RANK)
class UserRankFragment : BaseFragment<UserFragmentRankBinding, UserRankVm>() {

    override fun initContentView(): Int {
        return R.layout.user_fragment_rank
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun enableLazy(): Boolean {
        return false
    }

    override fun initData() {
        binding.ivAvatar.loadCircleImageRes(R.mipmap.ic_launcher)
        binding.tvUser.text = viewModel.model.getLoginName()
        binding.tvScore.text =
            String.format("积分：%s", arguments?.getString(AppConstants.BundleKey.USER_SCORE))
        binding.tvRank.text =
            String.format("排名：%s", arguments?.getString(AppConstants.BundleKey.USER_RANK))
        binding.smartCommon.autoRefresh()
    }

    override fun initViewObservable() {
        val mAdapter = UserRankAdapter()
        binding.ryCommon.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            showShimmerAdapter()
        }

        viewModel.uc.loadDataEvent.observe(this, { data ->
            if (data == null) {
                binding.ryCommon.hideShimmerAdapter()
                if (viewModel.currentPage == 1) binding.smartCommon.finishRefresh(false)
                else binding.smartCommon.finishLoadMore(false)
                return@observe
            }
            if (viewModel.currentPage == 1) {
                binding.ryCommon.hideShimmerAdapter()
                binding.smartCommon.finishRefresh(true)
                mAdapter.setDiffCallback(mAdapter.diffConfig)
                mAdapter.setDiffNewData(data.datas as MutableList<UserRankBean.Data>)
                if (data.over) binding.smartCommon.finishRefreshWithNoMoreData()
                return@observe
            }
            mAdapter.addData(data.datas)
            if (data.over) binding.smartCommon.finishLoadMoreWithNoMoreData()
            else binding.smartCommon.finishLoadMore()
        })
    }
}