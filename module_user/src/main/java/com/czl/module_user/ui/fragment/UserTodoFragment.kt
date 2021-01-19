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
import com.czl.module_user.widget.TodoFilterPopView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import java.util.*


/**
 * @author Alwyn
 * @Date 2021/1/5
 * @Description
 */
@Route(path = AppConstants.Router.User.F_USER_TODO)
class UserTodoFragment : BaseFragment<UserFragmentTodoBinding, UserTodoViewModel>() {

    private lateinit var mAdapter: UserTodoAdapter
    private val todoPopView: BasePopupView by inject(named("todo"))

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
            // 遍历设置时间是否过期
            data?.datas?.forEach { item ->
                if (item.date < TimeUtils.date2Millis(Date()) && !TimeUtils.isToday(item.date)) {
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
                data?.isOver, 1
            )
        })
        viewModel.uc.showAddTodoPopEvent.observe(this, {
            todoPopView.show()
        })
        LiveBusCenter.observeTodoListRefreshEvent(this, {
            if (it.code == 0) {
                val todoInfo = it.todoInfo
                // 查找是否有相同日期的数据存在
                updateList(todoInfo)
            }
        })
        viewModel.uc.showDrawerPopEvent.observe(this, {
            XPopup.Builder(context)
                .popupPosition(PopupPosition.Right)
                .hasStatusBar(false)
                .asCustom(TodoFilterPopView(this))
                .show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201 && resultCode == 200 && data != null) {
            val todoInfo =
                data.getParcelableExtra<TodoBean.Data>(AppConstants.BundleKey.TODO_INFO_DATA)
                    ?: return
            // 查找是否有相同日期的数据存在
            updateList(todoInfo)
        }
    }

    private fun updateList(todoInfo: TodoBean.Data) {
        val sameDateTodo = mAdapter.data.find { item -> item.dateStr == todoInfo.dateStr }
        if (sameDateTodo != null) {
            // 存在
            mAdapter.addData(mAdapter.getItemPosition(sameDateTodo), todoInfo)
            return
        }
        // 不存在相同日期  则查找最后一个未来日期添加到下面
        val moreDateTodo = mAdapter.data.findLast { item -> item.date > todoInfo.date }
        if (moreDateTodo != null) {
            mAdapter.addData(mAdapter.getItemPosition(moreDateTodo) + 1, todoInfo)
            return
        }
        // 不存在未来日期 则直接添加到第一个
        mAdapter.addData(0, todoInfo)
        viewModel.scrollToTop()
    }
}