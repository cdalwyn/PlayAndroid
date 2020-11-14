package com.czl.module_project.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.module_project.BR
import com.czl.module_project.R
import com.czl.module_project.adapter.ProjectItemGridAdapter
import com.czl.module_project.databinding.ProjectFragmentContentBinding
import com.czl.module_project.viewmodel.ContentViewModel


/**
 * @author Alwyn
 * @Date 2020/11/11
 * @Description 项目Tab Viewpager2+Fragment
 */
class ContentFragment : BaseFragment<ProjectFragmentContentBinding, ContentViewModel>() {

    private lateinit var mAdapter: ProjectItemGridAdapter

    companion object {
        const val SORT_ID = "sort_id"
        fun getInstance(id: Int): ContentFragment {
            val bundle = Bundle()
            bundle.putInt(SORT_ID, id)
            val contentFragment = ContentFragment()
            contentFragment.arguments = bundle
            return contentFragment
        }
    }

    override fun initContentView(): Int {
        return R.layout.project_fragment_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun enableSwipeBack(): Boolean {
        return false
    }

    override fun initData() {
        val sortId = arguments?.getInt(SORT_ID, 0)
        sortId?.let {
            binding.smartCommon.autoRefreshAnimationOnly()
            viewModel.cid = it
            viewModel.getProjectDataByCid()
        }
    }

    override fun initViewObservable() {
        initAdapter()
        // 接收加载完成的数据
        viewModel.uc.refreshCompleteEvent.observe(this, Observer {
            if (it == null) {
                binding.smartCommon.finishRefresh(300)
                binding.smartCommon.finishLoadMore(false)
                return@Observer
            }
            binding.smartCommon.finishRefresh(300)
            if (it.over) {
                binding.smartCommon.finishLoadMoreWithNoMoreData()
            } else {
                binding.smartCommon.finishLoadMore(true)
            }
            if (viewModel.currentPage > 1) {
                mAdapter.addData(it.datas)
                return@Observer
            }
            mAdapter.setDiffCallback(mAdapter.diffConfig)
            mAdapter.setDiffNewData(it.datas as MutableList<ProjectBean.Data>)
        })
        // 置顶
        viewModel.uc.moveTopEvent.observe(this, Observer {
            binding.ryCommon.smoothScrollToPosition(0)
        })
    }

    private fun initAdapter() {
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //解决item跳动
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        mAdapter = ProjectItemGridAdapter()
        binding.ryCommon.apply {
            layoutManager = manager
            adapter = mAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    // 解决滑到顶部留白问题
                    val first: IntArray = IntArray(2)
                    manager.findFirstCompletelyVisibleItemPositions(first)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                        manager.invalidateSpanAssignments()
                    }
                }
            })
        }
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as ProjectBean.Data
            startContainerActivity(
                AppConstants.Router.Base.F_WEB,
                Bundle().apply { putString(AppConstants.BundleKey.WEB_URL, item.link) })
        }
    }

}