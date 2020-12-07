package com.czl.module_user.ui.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.databinding.CommonRecycleviewBinding
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserBrowseAdapter
import com.czl.module_user.viewmodel.UserBrowseVm
import org.apache.commons.lang3.time.DateFormatUtils

/**
 * @author Alwyn
 * @Date 2020/12/7
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_BROWSE)
class UserBrowseFragment : BaseFragment<CommonRecycleviewBinding, UserBrowseVm>() {
    private lateinit var mAdapter:UserBrowseAdapter
    override fun initContentView(): Int {
        return R.layout.common_recycleview
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun enableLazy(): Boolean {
        return false
    }

    override fun initData() {
        viewModel.tvTitle.set("阅读历史")
        initAdapter()
        // todo
    }

    private fun initAdapter() {
        mAdapter = UserBrowseAdapter(this)
    }
}