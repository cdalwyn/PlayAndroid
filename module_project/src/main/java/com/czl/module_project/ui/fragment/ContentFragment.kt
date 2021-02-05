package com.czl.module_project.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.util.RxThreadHelper
import com.czl.module_project.BR
import com.czl.module_project.R
import com.czl.module_project.adapter.ProjectItemGridAdapter
import com.czl.module_project.databinding.ProjectFragmentContentBinding
import com.czl.module_project.viewmodel.ContentViewModel
import io.reactivex.Observable


/**
 * @author Alwyn
 * @Date 2020/11/11
 * @Description 项目Tab Viewpager2+Fragment
 */
class ContentFragment : BaseFragment<ProjectFragmentContentBinding, ContentViewModel>() {

    private lateinit var mAdapter: ProjectItemGridAdapter
    private var firstLoad = true

    companion object {
        const val SORT_ID = "sort_id"
        fun getInstance(id: String): ContentFragment = ContentFragment().apply {
            arguments = Bundle().apply {
                putString(SORT_ID, id)
            }
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

    override fun onResume() {
        super.onResume()
        // 懒加载
        if (firstLoad) {
            // 仅加载第一个fragment的数据缓存
            if (parentFragment is ProjectFragment && (parentFragment as ProjectFragment).binding.viewpager.currentItem == 0) {
                loadFirstPageCache()
                return
            }
            refreshData()
        }
    }

    private fun loadFirstPageCache() {
        viewModel.addSubscribe(Observable.create<List<ProjectBean.Data>> {
            it.onNext(viewModel.getCacheList())
        }.compose(RxThreadHelper.rxSchedulerHelper())
            .subscribe({ cacheList ->
            if (cacheList.isNotEmpty()) {
                firstLoad = false
                mAdapter.setDiffNewData(cacheList as MutableList<ProjectBean.Data>)
            } else {
                refreshData()
            }
        }) {
            it.printStackTrace()
            refreshData()
        })
    }

    private fun refreshData() {
        binding.smartCommon.autoRefresh()
    }

    override fun reload() {
        super.reload()
        refreshData()
    }

    override fun initData() {
        viewModel.cid = arguments?.getString(SORT_ID)
        initAdapter()
    }

    override fun initViewObservable() {
        // 接收加载完成的数据
        viewModel.uc.refreshCompleteEvent.observe(this, Observer {
            // 仅缓存第一个fragment的数据
            if (parentFragment is ProjectFragment && (parentFragment as ProjectFragment).binding.viewpager.currentItem == 0 && !it?.datas.isNullOrEmpty()) {
                viewModel.model.saveCacheListData(it!!.datas)
            }
            if (it == null) {
                binding.smartCommon.finishRefresh(500)
                binding.smartCommon.finishLoadMore(false)
                return@Observer
            }
            // 成功加载数据后关闭懒加载开关
            firstLoad = false
            binding.smartCommon.finishRefresh(500)
            if (it.over) {
                binding.smartCommon.finishLoadMoreWithNoMoreData()
            } else {
                binding.smartCommon.finishLoadMore(true)
            }
            if (viewModel.currentPage > 1) {
                mAdapter.addData(it.datas)
                return@Observer
            }
            mAdapter.setDiffNewData(it.datas as MutableList<ProjectBean.Data>)
        })
        // 置顶
        viewModel.uc.moveTopEvent.observe(this, {
            binding.ryCommon.smoothScrollToPosition(0)
        })
    }

    private fun initAdapter() {
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //解决item跳动
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        mAdapter = ProjectItemGridAdapter(this)
        mAdapter.setDiffCallback(mAdapter.diffConfig)
        binding.ryCommon.apply {
            layoutManager = manager
            adapter = mAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    // 解决滑到顶部留白问题
                    val first = IntArray(2)
                    manager.findFirstCompletelyVisibleItemPositions(first)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                        manager.invalidateSpanAssignments()
                    }
                }
            })
        }
    }
}