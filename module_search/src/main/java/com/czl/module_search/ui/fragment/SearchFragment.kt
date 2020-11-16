package com.czl.module_search.ui.fragment

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_search.BR
import com.czl.module_search.R
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

    private lateinit var rySkeletonScreen: SkeletonScreen

    override fun initContentView(): Int {
        return R.layout.search_fragment_search
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun enableLazy(): Boolean {
        return false
    }

    override fun onSupportVisible() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init()
    }

    override fun initData() {
        val keyword = arguments?.getString(AppConstants.BundleKey.MAIN_SEARCH_KEYWORD)
        keyword?.let {
            viewModel.keyword = it
            viewModel.searchPlaceHolder.set(it)
            rySkeletonScreen = Skeleton.bind(binding.smartCommon)
                .load(R.layout.search_item_skeleton)
                .show()
            viewModel.getSearchDataByKeyword(it)
        }
        binding.searchBar.isSuggestionsEnabled = false
    }

    override fun initViewObservable() {
        viewModel.uc.searchCancelEvent.observe(this, Observer {
            binding.searchBar.closeSearch()
        })
        viewModel.uc.finishLoadEvent.observe(this, Observer { over ->
            Handler(Looper.getMainLooper())
                .postDelayed({ rySkeletonScreen.hide() }, 300)
            binding.smartCommon.apply {
                finishRefresh(300)
                if (over) finishLoadMoreWithNoMoreData() else finishLoadMore()
            }
        })
        viewModel.uc.moveTopEvent.observe(this, Observer {
            binding.ryCommon.smoothScrollToPosition(0)
        })
    }


}