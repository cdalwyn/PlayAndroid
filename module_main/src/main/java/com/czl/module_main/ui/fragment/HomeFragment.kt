package com.czl.module_main.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ThreadUtils
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.HomeArticleBean
import com.czl.lib_base.data.bean.HomeBannerBean
import com.czl.lib_base.data.bean.SearchHotKeyBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.util.DayModeUtil
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.adapter.HomeArticleAdapter
import com.czl.module_main.adapter.HomeProjectAdapter
import com.czl.module_main.adapter.MyBannerAdapter
import com.czl.module_main.adapter.SearchSuggestAdapter
import com.czl.module_main.databinding.MainFragmentHomeBinding
import com.czl.module_main.viewmodel.HomeViewModel
import com.czl.module_main.widget.HomeDrawerPop
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.youth.banner.transformer.AlphaPageTransformer
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Alwyn
 * @Date 2020/11/27
 * @Description 首页
 */
@Route(path = AppConstants.Router.Main.F_HOME)
class HomeFragment : BaseFragment<MainFragmentHomeBinding, HomeViewModel>() {

    private lateinit var bannerAdapter: MyBannerAdapter
    private lateinit var suggestAdapter: SearchSuggestAdapter
    private lateinit var mHomeDrawerPop: HomeDrawerPop
    private lateinit var bannerSkeleton: SkeletonScreen
    lateinit var mArticleAdapter: HomeArticleAdapter
    lateinit var mProjectAdapter: HomeProjectAdapter
    val loginPopView: BasePopupView by inject(named("login"))
    private var changeSearchTask: Disposable? = null
    private var hotKeyList: List<String>? = null

    override fun onSupportVisible() {
        ImmersionBar.with(this).fitsSystemWindows(true)
            .statusBarDarkFont(!DayModeUtil.isNightMode(requireContext())).init()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingStatePage()
    }

    override fun initData() {
        super.initData()
        viewModel.tabSelectedPosition.set(0)
        initBanner()
        initSearchBar()
        initArticleAdapter()
        initProjectAdapter()
        // 加载保存时间为AppConstants.CacheKey.CACHE_SAVE_TIME_SECONDS的缓存 过期则刷新加载
        loadData()
    }

    private fun loadData() {
        val bannerObservable = Observable.create<List<HomeBannerBean?>> {
            it.onNext(viewModel.getCacheData(AppConstants.CacheKey.CACHE_HOME_BANNER))
        }.subscribeOn(Schedulers.io())
        val articleObservable = Observable.create<List<HomeArticleBean.Data?>> {
            it.onNext(viewModel.getCacheData(AppConstants.CacheKey.CACHE_HOME_ARTICLE))
        }.subscribeOn(Schedulers.io())
        val keywordObservable = Observable.create<List<SearchHotKeyBean>> {
            it.onNext(viewModel.getCacheData(AppConstants.CacheKey.CACHE_HOME_KEYWORD))
        }.subscribeOn(Schedulers.io())
        // merge合并后 按时间线并行执行  concat 合并会按发送顺序串行执行
        viewModel.addSubscribe(Observable.merge(
            bannerObservable,
            articleObservable,
            keywordObservable
        )
            .compose(RxThreadHelper.rxSchedulerHelper())
            .subscribe({
                showSuccessStatePage()
                if (!it.isNullOrEmpty()) {
                    when (it[0]) {
                        is HomeBannerBean? -> {
                            bannerSkeleton.hide()
                            bannerAdapter = MyBannerAdapter(it as List<HomeBannerBean?>, this)
                            binding.banner.adapter = bannerAdapter
                        }
                        is HomeArticleBean.Data? -> {
                            binding.ryArticle.hideShimmerAdapter()
                            mArticleAdapter.setDiffNewData(it as MutableList<HomeArticleBean.Data>?)
                        }
                        is SearchHotKeyBean -> {
                            setTimerHotKey(it as List<SearchHotKeyBean>)
                        }
                    }
                } else {
                    binding.smartCommon.autoRefresh()
                }
            }) {
                showSuccessStatePage()
                it.printStackTrace()
                binding.smartCommon.autoRefresh()
            })
    }

    override fun initViewObservable() {
        // 轮播图数据获取完成
        viewModel.uc.bannerCompleteEvent.observe(this) {
            bannerSkeleton.hide()
            if (!this::bannerAdapter.isInitialized) {
                // 第一次加载
                viewModel.model.saveCacheListData(it)
                bannerAdapter = MyBannerAdapter(it, this)
                binding.banner.adapter = bannerAdapter
            } else {
                bannerAdapter.setData(binding.banner, it)
            }
        }
        // 置顶
        viewModel.uc.moveTopEvent.observe(this) { tabPosition ->
            when (tabPosition) {
                0 -> binding.ryArticle.smoothScrollToPosition(0)
                1 -> binding.ryProject.smoothScrollToPosition(0)
            }
        }
        // 打开抽屉
        viewModel.uc.drawerOpenEvent.observe(this) {
            XPopup.Builder(context)
                .asCustom(mHomeDrawerPop)
                .show()
        }
        // 确认搜索后关闭焦点
        viewModel.uc.searchConfirmEvent.observe(this) {
            startSearch(it)
        }
        // 搜索历史记录item点击
        viewModel.uc.searchItemClickEvent.observe(this) {
            startSearch(suggestAdapter.suggestions[it])
        }
        // 搜素框历史记录Item删除
        viewModel.uc.searchItemDeleteEvent.observe(this) {
            setSuggestAdapterData()
            // 数据库同步
            viewModel.addSubscribe(viewModel.model.deleteSearchHistory(suggestAdapter.suggestions[it]))
        }
        // 接收搜索界面点击搜索的事件
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
        // 点击项目tab判断数据是否为空(第一次加载)
        viewModel.uc.tabSelectedEvent.observe(this) { position ->
            when (position) {
                1 -> {
                    if (mProjectAdapter.data.isNullOrEmpty()) {
                        viewModel.currentProjectPage = -1
                        viewModel.getProject()
                    }
                }
                0 -> {
                    if (mArticleAdapter.data.isNullOrEmpty()) {
                        viewModel.currentArticlePage = -1
                        viewModel.getArticle()
                    }
                }
            }
        }
        // 接收文章列表数据
        viewModel.uc.loadArticleCompleteEvent.observe(this) { data ->
            if (!data?.datas.isNullOrEmpty()) {
                viewModel.model.saveCacheListData(data!!.datas)
            }
            handleRecyclerviewData(
                data == null,
                data?.datas as MutableList<*>?,
                mArticleAdapter,
                binding.ryArticle,
                binding.smartCommon,
                viewModel.currentArticlePage,
                data?.over
            )
        }
        // 接收项目列表数据
        viewModel.uc.loadProjectCompleteEvent.observe(this) { data ->
            handleRecyclerviewData(
                data == null,
                data?.datas as MutableList<*>?,
                mProjectAdapter,
                binding.ryProject,
                binding.smartCommon,
                viewModel.currentProjectPage,
                data?.over
            )
        }
        // 接收收藏夹取消收藏事件
        LiveBusCenter.observeCollectStateEvent(this) { event ->
            // 同步两个列表存在相同文章的收藏状态
            val list = mArticleAdapter.data.filter { it.id == event.originId }
            if (list.isNotEmpty()) list[0].collect = false
            val filterList = mProjectAdapter.data.filter { it.id == event.originId }
            if (filterList.isNotEmpty()) filterList[0].collect = false
        }
        // 接收搜索热词
        viewModel.uc.loadSearchHotKeyEvent.observe(this) { list ->
            if (list.isNotEmpty()) {
                // 缓存
                viewModel.model.saveCacheListData(list)
                setTimerHotKey(list)
            }
        }
        // 搜索框右边图标点击事件
        viewModel.uc.searchIconClickEvent.observe(this) {
            if (getString(R.string.main_default_search_placeholder) == binding.searchBar.placeHolderText) {
                binding.searchBar.openSearch()
            } else {
//                suggestAdapter.addSuggestion(binding.searchBar.placeHolderText.toString())
                startSearch(binding.searchBar.placeHolderText.toString())
            }
        }
    }

    private fun setTimerHotKey(list: List<SearchHotKeyBean>) {
        hotKeyList = list.map { it.name }
        changeSearchTask?.dispose()
        // 发布定时任务更换搜索框关键字
        changeSearchTask = Flowable.interval(0, 10, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val hotKeyword = list[Random().nextInt(list.size)].name
                binding.searchBar.setPlaceHolder(hotKeyword)
            }) {
                it.printStackTrace()
                LogUtils.e("定时更换搜索热词失败")
            }
        viewModel.addSubscribe(changeSearchTask!!)
    }

    private fun initProjectAdapter() {
        mProjectAdapter = HomeProjectAdapter(this)
        mProjectAdapter.setDiffCallback(mProjectAdapter.diffConfig)
        binding.ryProject.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mProjectAdapter
            setDemoLayoutReference(R.layout.main_item_project_skeleton)
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            showShimmerAdapter()
        }
    }

    private fun initArticleAdapter() {
        mArticleAdapter = HomeArticleAdapter(this)
        mArticleAdapter.setDiffCallback(mArticleAdapter.diffConfig)
        binding.ryArticle.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mArticleAdapter
            setDemoLayoutReference(R.layout.main_article_item_skeleton)
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            showShimmerAdapter()
        }
    }

    private fun startSearch(keyword: String) {
        // 保存到数据库
        viewModel.addSubscribe(viewModel.model.saveUserSearchHistory(keyword).subscribe())
        if (binding.searchBar.isSearchOpened) binding.searchBar.closeSearch()
        val bundle = Bundle()
        bundle.putString(AppConstants.BundleKey.MAIN_SEARCH_KEYWORD, keyword)
        bundle.putStringArrayList(
            AppConstants.BundleKey.SEARCH_HOT_KEY_LIST,
            hotKeyList as ArrayList<String>?
        )
        startContainerActivity(AppConstants.Router.Search.F_SEARCH, bundle)
        setSuggestAdapterData()
    }

    override fun reload() {
        super.reload()
        binding.smartCommon.autoRefresh()
    }

    /**
     * 刷新搜索记录
     */
    private fun setSuggestAdapterData() {
        viewModel.addSubscribe(viewModel.model.getSearchHistoryByUid()
            .compose(RxThreadHelper.rxFlowSchedulerHelper())
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
        }
    }
}