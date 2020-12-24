package com.czl.module_search.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.event.LiveBusCenter
import com.czl.module_search.BR
import com.czl.module_search.R
import com.czl.module_search.adapter.SearchAdapter
import com.czl.module_search.adapter.SearchRecAdapter
import com.czl.module_search.databinding.SearchFragmentSearchBinding
import com.czl.module_search.viewmodel.SearchViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
@Route(path = AppConstants.Router.Search.F_SEARCH)
class SearchFragment : BaseFragment<SearchFragmentSearchBinding, SearchViewModel>() {
    private lateinit var mAdapter: SearchAdapter
    private lateinit var mRecAdapter: SearchRecAdapter
    private lateinit var ryRecommend: RecyclerView
    private var hotKeyList: ArrayList<String>? = null
    override fun initContentView(): Int {
        return R.layout.search_fragment_search
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun isThemeRedStatusBar(): Boolean {
        return true
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun enableLazy(): Boolean {
        return false
    }

    override fun initData() {
        initAdapter()
        val keyword = arguments?.getString(AppConstants.BundleKey.MAIN_SEARCH_KEYWORD)
        hotKeyList = arguments?.getStringArrayList(AppConstants.BundleKey.SEARCH_HOT_KEY_LIST)
        keyword?.let {
            viewModel.keyword = it
            viewModel.searchPlaceHolder.set(it)
            binding.includeRy.smartCommon.autoRefresh()
        }
        binding.searchBar.isSuggestionsEnabled = false
    }

    private fun initAdapter() {
        mAdapter = SearchAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.includeRy.ryCommon.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutReference(R.layout.search_item_skeleton)
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            showShimmerAdapter()
        }
    }

    override fun initViewObservable() {
        viewModel.uc.searchCancelEvent.observe(this, {
            binding.searchBar.closeSearch()
        })
        viewModel.uc.finishLoadEvent.observe(this, { data ->
            handleRecyclerviewData(
                data == null,
                data?.datas as MutableList<*>?,
                mAdapter,
                binding.includeRy.ryCommon,
                binding.includeRy.smartCommon,
                viewModel.currentPage,
                data?.over
            )
        })
        viewModel.uc.searchConfirmEvent.observe(this, {
            viewModel.searchPlaceHolder.set(it)
            viewModel.keyword = it
            viewModel.addSubscribe(viewModel.model.saveUserSearchHistory(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { saved ->
                    if (saved) LiveBusCenter.postSearchHistoryEvent()
                })
            binding.searchBar.closeSearch()
            if (this::mRecAdapter.isInitialized) {
                mRecAdapter.notifyDataSetChanged()
            }
            binding.includeRy.smartCommon.autoRefresh()
        })
        viewModel.uc.searchFocusEvent.observe(this, { focus ->
            // 搜索框获取到焦点显示热门搜索和历史搜索 布局使用viewstub延迟加载
            if (focus) {
                binding.includeRy.clRoot.visibility = View.GONE
                if (!this::ryRecommend.isInitialized) {
                    val inflate = binding.searchStub.viewStub?.inflate()
                    ryRecommend = inflate!!.findViewById(R.id.ry_rec_parent)
                    mRecAdapter =
                        SearchRecAdapter(this, hotKeyList, viewModel.model.getSearchHistoryByUid())
                    ryRecommend.apply {
                        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        adapter = mRecAdapter
                    }
                    mRecAdapter.setList(arrayListOf("热门搜索", "历史搜索"))
                } else {
                    binding.searchStub.viewStub?.visibility = View.VISIBLE
                }
            } else {
                binding.includeRy.clRoot.visibility = View.VISIBLE
                if (this::ryRecommend.isInitialized)
                    binding.searchStub.viewStub?.visibility = View.GONE
            }
        })
    }

    override fun reload() {
        super.reload()
        viewModel.getSearchDataByKeyword()
    }


}