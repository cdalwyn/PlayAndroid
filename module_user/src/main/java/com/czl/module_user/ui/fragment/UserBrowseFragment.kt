package com.czl.module_user.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.databinding.CommonRecyclerviewBinding
import com.czl.lib_base.util.DialogHelper
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserBrowseAdapter
import com.czl.module_user.viewmodel.UserBrowseVm
import com.google.android.material.snackbar.Snackbar
import com.lxj.xpopup.core.BasePopupView
import org.koin.android.ext.android.get
import org.koin.core.qualifier.named

/**
 * @author Alwyn
 * @Date 2020/12/7
 * @Description 阅读历史
 */
@Route(path = AppConstants.Router.User.F_USER_BROWSE)
class UserBrowseFragment : BaseFragment<CommonRecyclerviewBinding, UserBrowseVm>() {
    private lateinit var mAdapter: UserBrowseAdapter
    private lateinit var mSnackBar:Snackbar
    override fun initContentView(): Int {
        return R.layout.common_recyclerview
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("阅读历史")
        initAdapter()
        binding.smartCommon.autoRefresh()
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        // 未登录
        if (viewModel.model.getLoginName().isNullOrBlank()){
            if (this::mSnackBar.isInitialized){
                mSnackBar.show()
                return
            }
            mSnackBar = SnackbarUtils.with(binding.smartCommon)
                .setMessage("当前尚未登录，请登录后再试~")
                .setDuration(SnackbarUtils.LENGTH_INDEFINITE)
                .setBgColor(ContextCompat.getColor(requireContext(),R.color.black))
                .setMessageColor(ContextCompat.getColor(requireContext(),R.color.white))
                .setAction("登录"){
                    get<BasePopupView>(named("login")).show()
                }
                .show(true)
        }
    }

    override fun initViewObservable() {
        viewModel.loadCompleteEvent.observe(this) {
            viewModel.toolbarRightText.set(if (it.isEmpty()) "" else "清空")
            binding.smartCommon.finishRefreshWithNoMoreData()
            binding.ryCommon.hideShimmerAdapter()
            if (!mAdapter.hasEmptyView()) {
                val emptyView =
                    View.inflate(context, R.layout.common_empty_layout, null)
                emptyView.findViewById<ViewGroup>(R.id.ll_empty)
                    .setOnClickListener {
                        binding.smartCommon.autoRefresh()
                    }
                mAdapter.setEmptyView(emptyView)
            }
            mAdapter.setList(it)
        }
        viewModel.clearAllEvent.observe(this) {
            DialogHelper.showBaseDialog(requireContext(), "提示", "确定清空所有历史记录吗？") {
                viewModel.model.deleteAllWebHistory()
                    .compose(RxThreadHelper.rxSchedulerHelper(viewModel))
                    .subscribe {
                        mAdapter.setList(null)
                        viewModel.toolbarRightText.set("")
                    }
            }
        }
    }

    private fun initAdapter() {
        mAdapter = UserBrowseAdapter(this)
        binding.ryCommon.apply {
            ryCommon = this
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoChildCount(20)
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            setDemoLayoutReference(R.layout.user_item_browse_skeleton)
            showShimmerAdapter()
        }
    }
}