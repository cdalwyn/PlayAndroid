package com.czl.module_user.ui.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
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
            // 遍历设置时间文本的可见性达到时间分组效果
            data?.datas?.forEachIndexed { index, item ->
                if (index == 0) item.dateVisible = true
                if (index >= 1 && item.dateStr != data.datas[index - 1].dateStr) {
                    item.dateVisible = true
                }
                if (item.date < System.currentTimeMillis() && !TimeUtils.isToday(item.date)){
                    item.dateExpired = true
                }
            }
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
            val item = mAdapter.getItem(clickItemIndex)
            if (todoInfo != null && item != todoInfo) {
                if (item.dateStr == todoInfo.dateStr) {
                    // 如果时间是同一天则直接刷新
                    mAdapter.setData(clickItemIndex, todoInfo)
                } else {
                    // 不是同一天
                    binding.smartCommon.autoRefresh()
                    /*// 1. 先判断当前日期文本是否可见 若可见的则把下一项的日期先显示可见
                    if (item.dateVisible) {
                        mAdapter.getItem(clickItemIndex+1).dateVisible = true
                    }
                    // 2. 删除当前项
                    mAdapter.removeAt(clickItemIndex)
                    // 3. 在列表中查找与当前项日期是否相同的
                    val sameDateItem = mAdapter.data.findLast { it.dateStr == todoInfo.dateStr }
                    // 3.1 若存在相同的
                    if (sameDateItem != null) {
                        item.dateVisible = false
                        mAdapter.addData(
                            mAdapter.getItemPosition(sameDateItem),
                            todoInfo
                        )
                    } else {
                        // 3.2 不存在相同的
                        todoInfo.dateVisible = true
                        // 查找比当前日期久且最近的一个
                        val lessDateItem = mAdapter.data.find { it.date < todoInfo.date }
                        if (lessDateItem != null) {
                            mAdapter.addData(mAdapter.getItemPosition(lessDateItem), todoInfo)
                        } else {
                            // 若没有比当前日期久的 则查找离当前日期近的
                            val moreDateItem = mAdapter.data.findLast { it.date > todoInfo.date }
                            if (moreDateItem != null) {
                                mAdapter.addData(mAdapter.getItemPosition(moreDateItem) + 1, todoInfo)
                            } else {
                                // 若也没有离当前日期近的 则是日期最近的
                                mAdapter.setData(0, todoInfo)
                            }
                        }
                    }*/
                }
            }
        }
    }
}