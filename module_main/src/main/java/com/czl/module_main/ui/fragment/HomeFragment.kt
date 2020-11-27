package com.czl.module_main.ui.fragment


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.adapter.ViewPagerFmAdapter
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.adapter.HomeArticleAdapter
import com.czl.module_main.adapter.MyBannerAdapter
import com.czl.module_main.adapter.SearchSuggestAdapter
import com.czl.module_main.databinding.MainFragmentHomeBinding
import com.czl.module_main.viewmodel.HomeViewModel
import com.czl.module_main.widget.HomeDrawerPop
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import com.youth.banner.transformer.AlphaPageTransformer
import kotlinx.android.synthetic.main.main_fragment_home.*
import kotlinx.android.synthetic.main.main_layout_test.*


@Route(path = AppConstants.Router.Main.F_HOME)
class HomeFragment : BaseFragment<MainFragmentHomeBinding, HomeViewModel>() {

    private lateinit var bannerAdapter: MyBannerAdapter
    private lateinit var suggestAdapter: SearchSuggestAdapter

    private lateinit var mHomeDrawerPop: HomeDrawerPop
    private lateinit var bannerSkeleton: SkeletonScreen

    override fun onSupportVisible() {
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

    override fun enableSwipeBack(): Boolean {
        return false
    }

    override fun initData() {
        initBanner()
        initSearchBar()
        initViewPager()
        viewModel.getBanner()
    }


    override fun initViewObservable() {
        // 轮播图数据获取完成
        viewModel.uc.bannerCompleteEvent.observe(this, {
            bannerSkeleton.hide()
            if (!this::bannerAdapter.isInitialized) {
                bannerAdapter = MyBannerAdapter(it, this)
                binding.banner.setAdapter(bannerAdapter)
            } else {
                bannerAdapter.setData(binding.banner, it)
            }
            if (it == null) loadService.showWithConvertor(-1) else loadService.showWithConvertor(0)
        })
        // 打开抽屉
        viewModel.uc.drawerOpenEvent.observe(this, {
            XPopup.Builder(context)
                .asCustom(mHomeDrawerPop)
                .show()
        })
        // 确认搜索后关闭焦点
        viewModel.uc.searchConfirmEvent.observe(this, {
            setSuggestAdapterData()
            binding.searchBar.closeSearch()
            val bundle = Bundle()
            bundle.putString(AppConstants.BundleKey.MAIN_SEARCH_KEYWORD, it)
            startContainerActivity(AppConstants.Router.Search.F_SEARCH, bundle)
        })
        // 搜索框item点击
        viewModel.uc.searchItemClickEvent.observe(this, {
            binding.searchBar.closeSearch()
            val bundle = Bundle()
            bundle.putString(
                AppConstants.BundleKey.MAIN_SEARCH_KEYWORD,
                suggestAdapter.suggestions[it]
            )
            startContainerActivity(AppConstants.Router.Search.F_SEARCH, bundle)
        })
        // 搜素框Item删除
        viewModel.uc.searchItemDeleteEvent.observe(this, {
            setSuggestAdapterData()
            // 数据库同步
            viewModel.addSubscribe(viewModel.model.deleteSearchHistory(suggestAdapter.suggestions[it]))
        })
        // 注册搜索界面点击搜索的事件
        LiveBusCenter.observeSearchHistoryEvent(this) {
            if (it.code == 0) {
                setSuggestAdapterData()
            }
        }
        // 接收用户注销事件
        LiveBusCenter.observeLogoutEvent(this) {
            mHomeDrawerPop.binding?.user = null
        }
        // 接收用户登录成功事件
        LiveBusCenter.observeLoginSuccessEvent(this) {
            mHomeDrawerPop.binding?.user = viewModel.model.getUserData()
        }
    }


    override fun reload() {
        super.reload()
        viewModel.getBanner()
    }

    /**
     * 刷新搜索记录
     */
    private fun setSuggestAdapterData() {
        viewModel.addSubscribe(viewModel.model.getSearchHistoryByUid()
            .compose(RxThreadHelper.rxSchedulerHelper())
            .subscribe {
                suggestAdapter.suggestions = it.map { x -> x.history }
                if (suggestAdapter.suggestions.isEmpty()) binding.searchBar.hideSuggestionsList()
            })
    }

    private fun initSearchBar() {
        if (!this::mHomeDrawerPop.isInitialized) {
            mHomeDrawerPop = HomeDrawerPop(this)
        }
        if (!this::suggestAdapter.isInitialized) {
            suggestAdapter = SearchSuggestAdapter(layoutInflater)
            suggestAdapter.setListener(viewModel.onSearchItemClick)
            setSuggestAdapterData()
        }
        binding.searchBar.apply {
            setMaxSuggestionCount(5)
            setCustomSuggestionAdapter(suggestAdapter)
        }
    }

    private fun initBanner() {
        bannerSkeleton = Skeleton.bind(binding.banner)
            .load(R.layout.main_banner_skeleton)
            .show()
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

    private fun initViewPager() {
        val fragments = arrayListOf(HomeArticleFragment(), HomeProjectFragment())
        binding.viewpager.apply {
            adapter = ViewPagerFmAdapter(childFragmentManager, lifecycle, fragments)
            // 设置该属性后第一次将自动加载所有fragment 不配置该属性则使用viewpager2内部加载机制
            //                            offscreenPageLimit = fragments.size
        }
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            if (position == 0) tab.text = "热门博文"
            else tab.text = "热门项目"
        }.attach()

    }
}