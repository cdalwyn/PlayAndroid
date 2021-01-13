package com.czl.module_user.ui.fragment

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.TodoBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.adapter.UserTodoAdapter
import com.czl.module_user.databinding.UserFragmentTodoBinding
import com.czl.module_user.viewmodel.UserTodoViewModel
import com.lxj.xpopup.core.BasePopupView
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named


/**
 * @author Alwyn
 * @Date 2021/1/5
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_TODO)
class UserTodoFragment : BaseFragment<UserFragmentTodoBinding, UserTodoViewModel>() {

    private lateinit var mAdapter: UserTodoAdapter
    private val todoPopView: BasePopupView by inject(named("todo"))
    var clickItemIndex: Int = 0

    override fun initContentView(): Int {
        return R.layout.user_fragment_todo
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("待办清单")
        viewModel.ivToolbarIconRes.set(R.drawable.ic_nav)
        initAdapter()
        binding.smartCommon.autoRefresh()
    }

    private fun initAdapter() {
        mAdapter = UserTodoAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapter
            setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.LINEAR_VERTICAL)
            setDemoLayoutReference(R.layout.user_item_todo_skeleton)
            showShimmerAdapter()
        }
    }

    override fun initViewObservable() {
        viewModel.uc.refreshCompleteEvent.observe(this, { data ->
            handleRecyclerviewData(
                data == null,
                data?.datas as MutableList<*>?,
                mAdapter,
                binding.ryCommon,
                binding.smartCommon,
                viewModel.currentPage,
                data?.over, 1
            )
        })
        viewModel.uc.showAddTodoPopEvent.observe(this, {
            todoPopView.show()
        })
        LiveBusCenter.observeTodoListRefreshEvent(this, {
            binding.smartCommon.autoRefresh()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201 && resultCode == 200 && data != null) {
            val todoInfo =
                data.getParcelableExtra<TodoBean.Data>(AppConstants.BundleKey.TODO_INFO_DATA)
            if (todoInfo != null)
                mAdapter.setData(clickItemIndex, todoInfo)
        }
    }
}