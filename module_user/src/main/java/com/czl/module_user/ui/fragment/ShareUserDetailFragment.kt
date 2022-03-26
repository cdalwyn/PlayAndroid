package com.czl.module_user.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.ShareUserDetailBean
import com.czl.lib_base.extension.loadBlurImageRes
import com.czl.lib_base.util.DayModeUtil
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserDetailAdapter
import com.czl.module_user.databinding.UserDetailHeaderBinding
import com.czl.module_user.databinding.UserShareDetailBinding
import com.czl.module_user.viewmodel.ShareUserDetailVm
import com.ethanhua.skeleton.Skeleton
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.GrayscaleTransformation


/**
 * @author Alwyn
 * @Date 2020/12/19
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_DETAIL)
class ShareUserDetailFragment : BaseFragment<UserShareDetailBinding, ShareUserDetailVm>() {
    private var distanceY = 0
    private lateinit var userDetailAdapter: UserDetailAdapter
    private var headerDataBinding: UserDetailHeaderBinding? = null
    override fun initContentView(): Int {
        return R.layout.user_share_detail
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        initAdapter()
        initRvScroll()
        viewModel.userId = arguments?.getString(AppConstants.BundleKey.USER_ID)
        viewModel.userName = arguments?.getString(AppConstants.BundleKey.USER_NAME)
        viewModel.userId?.let {
            viewModel.currentPage = 0
            viewModel.getUserDetail()
        }
    }

    override fun initViewObservable() {
        val skeleton = Skeleton.bind(binding.smartCommon)
            .load(R.layout.user_detail_skeleton)
            .show()
        viewModel.uc.loadCompleteEvent.observe(this, { data ->
            ryCommon = binding.ryCommon
            val smartCommon = binding.smartCommon
            if (data == null) {
                skeleton.hide()
                smartCommon.finishRefresh(false)
                smartCommon.finishLoadMore(false)
                return@observe
            }
            if (viewModel.currentPage == 1) {
                skeleton.hide()
                viewModel.tvTitle.set(data.coinInfo.username)
                headerDataBinding?.apply {
                    user = data.coinInfo
                    group.visibility = View.VISIBLE
                    executePendingBindings()
                }
                if (!userDetailAdapter.hasEmptyView()) {
                    val emptyView =
                        View.inflate(context, R.layout.common_empty_layout, null)
                    emptyView.findViewById<ViewGroup>(R.id.ll_empty)
                        .setOnClickListener {
                            smartCommon.autoRefresh()
                        }
                    userDetailAdapter.setEmptyView(emptyView)
                }
                userDetailAdapter.setDiffNewData(data.shareArticles.datas as MutableList<ShareUserDetailBean.ShareArticles.Data>)
                if (data.shareArticles.over) smartCommon.finishRefreshWithNoMoreData()
                else smartCommon.finishRefresh(true)
                return@observe
            }
            if (data.shareArticles.over) smartCommon.finishLoadMoreWithNoMoreData()
            else smartCommon.finishLoadMore(true)
            userDetailAdapter.addData(data.shareArticles.datas)
        })
    }

    private fun initAdapter() {
        userDetailAdapter = UserDetailAdapter(this)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = userDetailAdapter
        }
        val headerView = View.inflate(context, R.layout.user_detail_header, null)
        headerDataBinding = DataBindingUtil.bind(headerView)
        userDetailAdapter.addHeaderView(headerView)
        userDetailAdapter.setDiffCallback(userDetailAdapter.diffConfig)
        headerDataBinding?.ivPlaceholder?.loadBlurImageRes(R.drawable.bg_user_detail)
    }

    private fun initRvScroll() {
        val homeToolbar = binding.includeToolbar.homeToolbar
        binding.includeToolbar.clRoot.setPadding(0, SizeUtils.dp2px(18f), 0, 0)
        binding.ryCommon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                distanceY += dy
                val layoutManager = binding.ryCommon.layoutManager as LinearLayoutManager
                // 第一个可见Item的位置
                val position: Int = layoutManager.findFirstVisibleItemPosition()
                if (position == 0) {
                    // 注意此操作如果第一项划出屏幕外,拿到的是空的，所以必须是position是0的时候才能调用
                    val firstView = layoutManager.findViewByPosition(position)
                    // 第一项Item的高度
                    val firstHeight: Int = firstView!!.height
                    // 要在它滑到二分之一的时候去渐变
                    val changeHeight = firstHeight / 2
                    // 小于头部高度一半隐藏标题栏
                    if (distanceY <= changeHeight) {
                        homeToolbar.visibility = View.GONE
                        // 渐变的区域，头部从中间到底部的距离
                    } else {
                        homeToolbar.visibility = View.VISIBLE
                        // 从高度的一半开始算透明度，也就是说移动到头部Item的中部，透明度从0开始计算
                        val scale = (distanceY - changeHeight).toFloat() / changeHeight
                        homeToolbar.alpha = scale
                    }
                    // 其他的时候就设置都可见，透明度是1
                } else {
                    homeToolbar.visibility = View.VISIBLE
                    homeToolbar.alpha = 1f
                }
            }
        })
    }

    override fun reload() {
        super.reload()
        binding.smartCommon.autoRefresh()
    }

    override fun onDestroyView() {
        headerDataBinding?.unbind()
        super.onDestroyView()
    }
}