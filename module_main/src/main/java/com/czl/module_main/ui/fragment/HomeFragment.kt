package com.czl.module_main.ui.fragment


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.adapter.MyBannerAdapter
import com.czl.module_main.adapter.SearchSuggestAdapter
import com.czl.module_main.databinding.MainFragmentHomeBinding
import com.czl.module_main.viewmodel.HomeViewModel
import com.czl.module_main.widget.HomeDrawerPop
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import com.youth.banner.transformer.AlphaPageTransformer


@Route(path = AppConstants.Router.Main.F_HOME)
class HomeFragment : BaseFragment<MainFragmentHomeBinding, HomeViewModel>() {

    private lateinit var bannerAdapter: MyBannerAdapter
    private lateinit var suggestAdapter: SearchSuggestAdapter

    private lateinit var homeDrawerPop: HomeDrawerPop
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


    override fun initData() {
        ryArticleSkeleton = Skeleton.bind(binding.ryArticle as View)
            .load(R.layout.main_article_item_skeleton)
            .show()
        bannerSkeleton = Skeleton.bind(binding.banner)
            .load(R.layout.main_banner_skeleton)
            .show()

        if (!this::homeDrawerPop.isInitialized) {
            homeDrawerPop = HomeDrawerPop(this)
        }
        if (!this::suggestAdapter.isInitialized) {
            suggestAdapter = SearchSuggestAdapter(layoutInflater)
            suggestAdapter.setListener(viewModel.onSearchItemClick)
            if (viewModel.model.getSearchHistory().isNotEmpty()) {
                suggestAdapter.suggestions = viewModel.model.getSearchHistory()
            }
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
                    if (viewModel.tabSelectedPosition.get() == 0) {
                        Handler(Looper.getMainLooper())
                            .postDelayed({ ryArticleSkeleton.hide() }, 600)
                    } else {
                        Handler(Looper.getMainLooper())
                            .postDelayed({ ryProjectSkeleton.hide() }, 600)
                    }
                }
                1 -> {
                }
                2 -> {
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
                .asCustom(homeDrawerPop)
                .show()
        })
        // 确认搜索后关闭焦点
        viewModel.uc.searchConfirmEvent.observe(this, Observer {
            suggestAdapter.addSuggestion(it)
            viewModel.model.saveSearchHistory(suggestAdapter.suggestions)
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
            suggestAdapter.deleteSuggestion(it, suggestAdapter.suggestions[it])
            if (suggestAdapter.suggestions.isEmpty()) binding.searchBar.hideSuggestionsList()
            viewModel.model.saveSearchHistory(suggestAdapter.suggestions)
        })
        // 第一次加载项目列表展示骨架屏
        viewModel.uc.firstLoadProjectEvent.observe(this, Observer {
            ryProjectSkeleton = Skeleton.bind(binding.ryProject as View)
                .load(R.layout.main_item_project_skeleton)
                .show()
        })
    }

}