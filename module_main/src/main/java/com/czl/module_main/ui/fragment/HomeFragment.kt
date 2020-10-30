package com.czl.module_main.ui.fragment

import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.entity.HomeBannerBean
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.adapter.MyBannerAdapter
import com.czl.module_main.databinding.MainFragmentHomeBinding
import com.czl.module_main.viewmodel.HomeViewModel
import com.flyco.tablayout.listener.CustomTabEntity
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.indicator.RoundLinesIndicator


@Route(path = AppConstants.Router.Main.F_HOME)
class HomeFragment : BaseFragment<MainFragmentHomeBinding, HomeViewModel>() {

    private lateinit var bannerAdapter: MyBannerAdapter
    private var bannerFlag = false
    private var rvFlag = false

    override fun initContentView(): Int {
        return R.layout.main_fragment_home
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        binding.refreshLayout.autoRefresh()
        binding.banner.apply {
            addBannerLifecycleObserver(this@HomeFragment)
            indicator = CircleIndicator(context).apply {
                indicatorConfig.gravity = IndicatorConfig.Direction.RIGHT
            }
        }
    }

    override fun initViewObservable() {
        viewModel.uc.bannerCompleteEvent.observe(this, Observer {
            bannerFlag = true
            if (!this::bannerAdapter.isInitialized) {
                bannerAdapter = MyBannerAdapter(it)
                binding.banner.setAdapter(bannerAdapter)
            } else {
                bannerAdapter.setData(binding.banner, it)
            }
            if (rvFlag && bannerFlag) {
                binding.refreshLayout.finishRefresh()
            }
        })
        viewModel.uc.rvLoadCompleteEvent.observe(this, Observer {
            rvFlag = true
            if (rvFlag && bannerFlag) {
                binding.refreshLayout.finishRefresh()
            }
        })
    }
}