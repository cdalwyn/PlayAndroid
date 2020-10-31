package com.czl.module_main.ui.fragment


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.extension.ImagePopLoader
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.adapter.MyBannerAdapter
import com.czl.module_main.databinding.MainFragmentHomeBinding
import com.czl.module_main.viewmodel.HomeViewModel
import com.czl.module_main.widget.ProjectItemSettingPop
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator


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
                bannerAdapter = MyBannerAdapter(it, this)
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
        viewModel.uc.picShowEvent.observe(this, Observer {
            XPopup.Builder(context)
                .asImageViewer(it.get(1) as ImageView, it.get(0), ImagePopLoader())
                .show()
        })
        val projectItemSettingPop = ProjectItemSettingPop(requireContext())
        viewModel.uc.settingShowEvent.observe(this, Observer {
            val popView = XPopup.Builder(context).hasShadowBg(false).atView(it.get(2) as View)
                .asCustom(projectItemSettingPop)
            popView.show()
            projectItemSettingPop.binding?.apply {
                tvCollect.setOnClickListener {
                    popView.dismiss()
                }
                tvSame.setOnClickListener {
                    popView.dismiss()
                }
            }
        })
    }
}