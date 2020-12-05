package com.czl.module_search.ui.fragment

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_search.BR
import com.czl.module_search.R
import com.czl.module_search.adapter.SearchAdapter
import com.czl.module_search.databinding.SearchFragmentSearchBinding
import com.czl.module_search.viewmodel.SearchViewModel
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.gyf.immersionbar.ImmersionBar

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
@Route(path = AppConstants.Router.Search.F_SEARCH)
class SearchFragment : BaseFragment<SearchFragmentSearchBinding, SearchViewModel>() {
    private lateinit var mAdapter:SearchAdapter
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
        viewModel.uc.searchCancelEvent.observe(this, Observer {
            binding.searchBar.closeSearch()
        })
        viewModel.uc.finishLoadEvent.observe(this, Observer { data ->
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
        viewModel.uc.searchConfirmEvent.observe(this,{
            binding.includeRy.smartCommon.autoRefresh()
        })

    }

    override fun reload() {
        super.reload()
        viewModel.getSearchDataByKeyword()
    }


}