package com.czl.module_main.ui.fragment


import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.adapter.MyBannerAdapter
import com.czl.module_main.databinding.MainFragmentHomeBinding
import com.czl.module_main.viewmodel.HomeViewModel
import com.czl.module_main.widget.HomeDrawerPop
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import com.youth.banner.transformer.AlphaPageTransformer


@Route(path = AppConstants.Router.Main.F_HOME)
class HomeFragment : BaseFragment<MainFragmentHomeBinding, HomeViewModel>() {

    private lateinit var bannerAdapter: MyBannerAdapter
    private var bannerFlag = false
    private var rvFlag = false
    private lateinit var homeDrawerPop: HomeDrawerPop

    override fun onSupportVisible() {
        super.onSupportVisible()
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init()
    }

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
        if (!this::homeDrawerPop.isInitialized) {
            homeDrawerPop = HomeDrawerPop(this)
        }
        binding.refreshLayout.autoRefresh()
        binding.banner.apply {
            addBannerLifecycleObserver(this@HomeFragment)
//            setBannerGalleryMZ(20)
            setBannerGalleryEffect(18, 10)
            addPageTransformer(AlphaPageTransformer(0.6f))
//            indicator = CircleIndicator(context).apply {
//                indicatorConfig.gravity = IndicatorConfig.Direction.RIGHT
//            }
        }
    }

    override fun initViewObservable() {
        // 轮播图数据获取完成
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
                rvFlag = !rvFlag
                bannerFlag = !bannerFlag
            }
        })
        // 下拉刷新完成
        viewModel.uc.refreshCompleteEvent.observe(this, Observer {
            rvFlag = true
            if (rvFlag && bannerFlag) {
                binding.refreshLayout.finishRefresh()
                rvFlag = !rvFlag
                bannerFlag = !bannerFlag
            }
        })
        // 列表加载更多完成
        viewModel.uc.loadCompleteEvent.observe(this, Observer {
            binding.refreshLayout.finishLoadMore()
        })
        // 置顶
        viewModel.uc.moveTopEvent.observe(this, Observer { tabPosition ->
            when (tabPosition) {
                0 -> binding.ryArticle.smoothScrollToPosition(0)
                1 -> binding.ryProject.smoothScrollToPosition(0)
            }
        })
        // 打开抽屉
        viewModel.uc.drawerOpenEvent.observe(this, Observer {
            XPopup.Builder(context)
                .asCustom(homeDrawerPop)
                .show()
        })

    }
}