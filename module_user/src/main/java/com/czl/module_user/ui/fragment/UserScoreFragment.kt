package com.czl.module_user.ui.fragment

import android.animation.ValueAnimator
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.UserFragmentScoreBinding
import com.czl.module_user.viewmodel.UserScoreVm
import com.ethanhua.skeleton.Skeleton


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

    override fun initViewObservable() {

        viewModel.uc.loadCompleteEvent.observe(this, { over ->
            binding.smartCommon.finishRefresh()
            if (over) {
                binding.smartCommon.finishLoadMoreWithNoMoreData()
            } else {
                binding.smartCommon.finishLoadMore()
            }
        })
        viewModel.uc.moveTopEvent.observe(this, {
            binding.ryCommon.smoothScrollToPosition(0)
        })

        viewModel.uc.getTotalScoreEvent.observe(this, {
            val animator = ValueAnimator.ofInt(0, it)
            animator.duration = 1200L
            animator.addUpdateListener { animation ->
                binding.tvScore.text = animation.animatedValue.toString()
            }
            animator.start()
        })

    }

}