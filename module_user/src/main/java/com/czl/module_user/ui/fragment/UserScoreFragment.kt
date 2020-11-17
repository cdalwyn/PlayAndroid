package com.czl.module_user.ui.fragment

import android.animation.ValueAnimator
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.UserScoreDetailBean
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserScoreAdapter
import com.czl.module_user.databinding.UserFragmentScoreBinding
import com.czl.module_user.viewmodel.UserScoreVm
import com.ethanhua.skeleton.Skeleton
import com.gyf.immersionbar.ImmersionBar


@Route(path = AppConstants.Router.User.F_USER_SCORE)
class UserScoreFragment : BaseFragment<UserFragmentScoreBinding, UserScoreVm>() {
    override fun initContentView(): Int {
        return R.layout.user_fragment_score
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.smartCommon.autoRefresh()
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun onSupportVisible() {
        ImmersionBar.with(this).statusBarDarkFont(false).init()
    }

    override fun initViewObservable() {
        val mAdapter = UserScoreAdapter()
        binding.ryCommon.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.ryCommon.adapter = mAdapter
        // 显示骨架屏
        binding.ryCommon.showShimmerAdapter()
        // 接收刷新完成事件
        viewModel.uc.loadCompleteEvent.observe(this, { over ->
            if (viewModel.currentPage == 1) {
                binding.ryCommon.hideShimmerAdapter()
                binding.smartCommon.finishRefresh()
            }
            if (over) {
                binding.smartCommon.finishLoadMoreWithNoMoreData()
            } else {
                binding.smartCommon.finishLoadMore()
            }
        })
        // 置顶
        viewModel.uc.moveTopEvent.observe(this, {
            binding.ryCommon.smoothScrollToPosition(0)
        })
        // 总积分动画
        viewModel.uc.getTotalScoreEvent.observe(this, {
            val animator = ValueAnimator.ofInt(0, it)
            animator.duration = 1200L
            animator.addUpdateListener { animation ->
                binding.tvScore.text = animation.animatedValue.toString()
            }
            animator.start()
        })
        // 接收列表数据
        viewModel.uc.loadDataFinishEvent.observe(this, { list ->
            if (viewModel.currentPage > 1) {
                mAdapter.addData(list)
                return@observe
            }
            mAdapter.setDiffCallback(mAdapter.diffConfig)
            mAdapter.setDiffNewData(list as MutableList<UserScoreDetailBean.Data>)
        })

    }

}