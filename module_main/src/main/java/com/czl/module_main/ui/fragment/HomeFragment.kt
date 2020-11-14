package com.czl.module_main.ui.fragment


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.adapter.MyBannerAdapter
import com.czl.module_main.adapter.SearchSuggestAdapter
import com.czl.module_main.databinding.MainFragmentHomeBinding
import com.czl.module_main.viewmodel.HomeViewModel
import com.czl.module_main.widget.HomeDrawerPop
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import com.youth.banner.transformer.AlphaPageTransformer


@Route(path = AppConstants.Router.Main.F_HOME)
class HomeFragment : BaseFragment<MainFragmentHomeBinding, HomeViewModel>() {

    private lateinit var bannerAdapter: MyBannerAdapter
    private lateinit var suggestAdapter: SearchSuggestAdapter

    private lateinit var mHomeDrawerPop: HomeDrawerPop
    private lateinit var ryArticleSkeleton: SkeletonScreen
    private lateinit var ryProjectSkeleton: SkeletonScreen
    private lateinit var bannerSkeleton: SkeletonScreen

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

    override fun enableSwipeBack(): Boolean {
        return false
    }

    override fun initData() {
        ryArticleSkeleton = Skeleton.bind(binding.ryArticle as View)
            .load(R.layout.main_article_item_skeleton)
            .show()
        bannerSkeleton = Skeleton.bind(binding.banner)
            .load(R.layout.main_banner_skeleton)
            .show()

        if (!this::mHomeDrawerPop.isInitialized) {
            mHomeDrawerPop = HomeDrawerPop(this)
        }
        if (!this::suggestAdapter.isInitialized) {
            suggestAdapter = SearchSuggestAdapter(layoutInflater)
            suggestAdapter.setListener(viewModel.onSearchItemClick)
            setSuggestAdapterData()
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
        binding.searchBar.apply {
            setMaxSuggestionCount(5)
            setCustomSuggestionAdapter(suggestAdapter)
        }
    }

    override fun initViewObservable() {
        // 轮播图数据获取完成
        viewModel.uc.bannerCompleteEvent.observe(this, Observer {
            bannerSkeleton.hide()
            if (!this::bannerAdapter.isInitialized) {
                bannerAdapter = MyBannerAdapter(it, this)
                binding.banner.setAdapter(bannerAdapter)
            } else {
                bannerAdapter.setData(binding.banner, it)
            }

        })
        // 下拉刷新的状态
        viewModel.uc.refreshStateEvent.observe(this, Observer { state ->
            when (state) {
                0 -> {
                    binding.refreshLayout.finishRefresh(true)
                    hideSkeletonByTabIndex()
                }
                1 -> {
                }
                2 -> {
                    binding.refreshLayout.finishRefresh(false)
                    hideSkeletonByTabIndex()
                }
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
                .asCustom(mHomeDrawerPop)
                .show()
        })
        // 确认搜索后关闭焦点
        viewModel.uc.searchConfirmEvent.observe(this, Observer {
            setSuggestAdapterData()
            binding.searchBar.closeSearch()
            val bundle = Bundle()
            bundle.putString(AppConstants.BundleKey.MAIN_SEARCH_KEYWORD, it)
            startContainerActivity(AppConstants.Router.Search.F_SEARCH, bundle)
        })
        // 搜索框item点击
        viewModel.uc.searchItemClickEvent.observe(this, Observer {
            binding.searchBar.closeSearch()
            val bundle = Bundle()
            bundle.putString(
                AppConstants.BundleKey.MAIN_SEARCH_KEYWORD,
                suggestAdapter.suggestions[it]
            )
            startContainerActivity(AppConstants.Router.Search.F_SEARCH, bundle)
        })
        // 搜素框Item删除
        viewModel.uc.searchItemDeleteEvent.observe(this, Observer {
            setSuggestAdapterData()
            // 数据库同步
            viewModel.addSubscribe(viewModel.model.deleteSearchHistory(suggestAdapter.suggestions[it]))
        })
        // 第一次加载项目列表展示骨架屏
        viewModel.uc.firstLoadProjectEvent.observe(this, Observer {
            ryProjectSkeleton = Skeleton.bind(binding.ryProject as View)
                .load(R.layout.main_item_project_skeleton)
                .show()
        })
        // 注册搜索界面点击搜索的事件
        LiveBusCenter.observeSearchHistoryEvent(this) {
            if (it.code == 0) {
                setSuggestAdapterData()
            }
        }
        viewModel.uc.tabSelectedEvent.observe(this, Observer {
            if (it == 0) {
                ryProjectSkeleton.hide()
            } else {
                ryArticleSkeleton.hide()
            }
        })
        // 接收用户注销事件
        LiveBusCenter.observeLogoutEvent(this){
            mHomeDrawerPop.binding?.user = null
        }
        // 接收用户登录成功事件
        LiveBusCenter.observeLoginSuccessEvent(this){
            mHomeDrawerPop.binding?.user = viewModel.model.getUserData()
        }
    }



    private fun hideSkeletonByTabIndex() {
        Handler(Looper.getMainLooper())
            .postDelayed({
                if (viewModel.tabSelectedPosition.get() == 0) ryArticleSkeleton.hide()
                else ryProjectSkeleton.hide()
            }, 300)
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
}