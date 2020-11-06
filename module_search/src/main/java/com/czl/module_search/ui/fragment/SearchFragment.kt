package com.czl.module_search.ui.fragment

import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_search.BR
import com.czl.module_search.R
import com.czl.module_search.databinding.SearchFragmentSearchBinding
import com.czl.module_search.viewmodel.SearchViewModel
import com.gyf.immersionbar.ImmersionBar

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
@Route(path = AppConstants.Router.Search.F_SEARCH)
class SearchFragment : BaseFragment<SearchFragmentSearchBinding, SearchViewModel>() {

    override fun initContentView(): Int {
        return R.layout.search_fragment_search
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun onSupportVisible() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init()
    }

    override fun initData() {
        val keyword = arguments?.getString(AppConstants.BundleKey.MAIN_SEARCH_KEYWORD)
        keyword?.let {
            viewModel.keyword = it
            binding.searchBar.setPlaceHolder(it)
            viewModel.getSearchDataByKeyword(it)
        }
    }

    override fun initViewObservable() {
        viewModel.uc.searchCancelEvent.observe(this, Observer {
            binding.searchBar.closeSearch()
        })
        viewModel.uc.refreshEvent.observe(this, Observer {
            binding.smartCommon.autoRefresh()
        })
        viewModel.uc.finishLoadEvent.observe(this, Observer {
            binding.smartCommon.apply {
                finishRefresh()
                finishLoadMore()
            }
        })
    }


}