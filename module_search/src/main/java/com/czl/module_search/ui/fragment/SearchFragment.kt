package com.czl.module_search.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.util.PermissionUtil
import com.czl.module_search.BR
import com.czl.module_search.R
import com.czl.module_search.databinding.SearchFragmentSearchBinding
import com.czl.module_search.viewmodel.SearchViewModel
import com.permissionx.guolindev.callback.RequestCallback
import me.goldze.mvvmhabit.utils.ToastUtils

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

    override fun initData() {

    }

    override fun useBaseLayout(): Boolean {
        return false
    }

}